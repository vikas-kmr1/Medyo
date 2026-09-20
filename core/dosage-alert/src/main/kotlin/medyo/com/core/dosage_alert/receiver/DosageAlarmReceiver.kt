package medyo.com.core.dosage_alert.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import medyo.com.core.database.dao.DosageAlertDao
import medyo.com.core.database.entity.DosageHistoryEntity
import medyo.com.core.database.entity.DosageStatus
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.scheduler.DosageAlarmSchedulerImpl
import medyo.com.core.dosage_alert.util.NextAlarmCalculator
import medyo.com.core.dosage_alert.ui.DosageFullScreenActivity
import medyo.com.core.dosage_alert.worker.AutoSkipWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DosageAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dosageAlertDao: DosageAlertDao

    @Inject
    lateinit var dosageAlarmScheduler: DosageAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        val scheduleId = intent.getLongExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULE_ID, -1L)
        val medicationId = intent.getLongExtra(DosageAlarmSchedulerImpl.EXTRA_MEDICATION_ID, -1L)
        val scheduledTimestamp =
            intent.getLongExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULED_TIMESTAMP, -1L)

        if (scheduleId == -1L || medicationId == -1L) {
            Log.e("DosageAlarmReceiver", "Invalid alarm data received.")
            return
        }

        Log.d(
            "DosageAlarmReceiver",
            "Received alarm for medication $medicationId, schedule $scheduleId"
        )

        // Create FullScreen Intent to launch DosageFullScreenActivity
        val fullScreenIntent = Intent(context, DosageFullScreenActivity::class.java).apply {
            putExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULE_ID, scheduleId)
            putExtra(DosageAlarmSchedulerImpl.EXTRA_MEDICATION_ID, medicationId)
            putExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULED_TIMESTAMP, scheduledTimestamp)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        val pendingIntent = android.app.PendingIntent.getActivity(
            context,
            scheduleId.hashCode(),
            fullScreenIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(
            context,
            medyo.com.core.notification.NotificationConstants.CHANNEL_ID_DOSAGE_ALERTS
        )
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Time to take your medication!")
            .setContentText("Tap to view details")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)

        notificationManager.notify(scheduleId.hashCode(), notificationBuilder.build())

        try {
            if (Settings.canDrawOverlays(context)) {
                context.startActivity(fullScreenIntent)
            }
        } catch (e: Exception) {
            Log.e("DosageAlarmReceiver", "Failed to start full screen activity directly", e)
        }

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create PENDING DosageHistoryEntity
                val historyId = dosageAlertDao.insertDosageHistory(
                    DosageHistoryEntity(
                        medicationId = medicationId,
                        scheduleId = scheduleId,
                        scheduledTimestamp = scheduledTimestamp,
                        actualTakenTimestamp = null,
                        status = DosageStatus.PENDING
                    )
                )

                // Compute and schedule the next day's alarm
                val schedule = dosageAlertDao.getScheduleById(scheduleId)
                if (schedule != null) {
                    val nextAlarm = NextAlarmCalculator.computeNextAlarmMillis(
                        schedule.timeOfDay,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.frequency
                    )
                    if (nextAlarm != null) {
                        dosageAlarmScheduler.scheduleDosageAlarm(scheduleId, medicationId, nextAlarm)
                    }
                }

                // Enqueue AutoSkipWorker with 30 minutes delay
                val inputData = Data.Builder()
                    .putLong(DosageAlarmSchedulerImpl.EXTRA_SCHEDULE_ID, scheduleId)
                    .putLong(DosageAlarmSchedulerImpl.EXTRA_MEDICATION_ID, medicationId)
                    .putLong("EXTRA_HISTORY_ID", historyId)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<AutoSkipWorker>()
                    .setInitialDelay(30, TimeUnit.MINUTES)
                    .setInputData(inputData)
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)

            } catch (e: Exception) {
                Log.e("DosageAlarmReceiver", "Failed to process alarm receiver logic", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}

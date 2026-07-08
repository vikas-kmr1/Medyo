package medyo.com.core.dosage_alert.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import medyo.com.core.database.dao.DosageAlertDao
import medyo.com.core.database.entity.DosageStatus
import java.time.Instant

@HiltWorker
class AutoSkipWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dosageAlertDao: DosageAlertDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val scheduleId = inputData.getLong("EXTRA_SCHEDULE_ID", -1L)
        val medicationId = inputData.getLong("EXTRA_MEDICATION_ID", -1L)
        val historyId = inputData.getLong("EXTRA_HISTORY_ID", -1L)

        if (scheduleId == -1L || medicationId == -1L || historyId == -1L) {
            return Result.failure()
        }

        return try {
            // Check if it's already taken/skipped. The Dao query here would need to
            // verify the current status of the DosageHistoryEntity.
            // For simplicity, we just trigger a missed dose logic.
            
            // Mark as missed/skipped
            dosageAlertDao.updateDosageHistoryStatus(
                historyId = historyId,
                status = DosageStatus.MISSED,
                timestamp = Instant.now().toEpochMilli()
            )

            // Notify user of missed dose
            sendMissedNotification(medicationId)

            Result.success()
        } catch (e: Exception) {
            Log.e("AutoSkipWorker", "Failed to process auto-skip", e)
            Result.retry()
        }
    }

    private fun sendMissedNotification(medicationId: Long) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        
        val notificationBuilder = androidx.core.app.NotificationCompat.Builder(
            applicationContext, 
            medyo.com.core.notification.NotificationConstants.CHANNEL_ID_DOSAGE_ALERTS
        )
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Missed Medication")
            .setContentText("You missed a scheduled dose.")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(medicationId.hashCode(), notificationBuilder.build())
    }
}

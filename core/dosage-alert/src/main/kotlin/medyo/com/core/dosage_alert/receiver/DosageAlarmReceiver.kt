package medyo.com.core.dosage_alert.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.scheduler.DosageAlarmSchedulerImpl
import medyo.com.core.dosage_alert.ui.DosageFullScreenActivity

@AndroidEntryPoint
class DosageAlarmReceiver : BroadcastReceiver() {
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

        // Send a High Priority Notification with the full-screen intent
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

        // As a fallback, we also directly start the activity
        try {
            if (Settings.canDrawOverlays(context)) {
                context.startActivity(fullScreenIntent)
            }
        } catch (e: Exception) {
            Log.e("DosageAlarmReceiver", "Failed to start full screen activity directly", e)
        }
    }
}

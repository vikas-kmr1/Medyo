package medyo.com.core.expiryalert

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.notification.NotificationConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpiryNotificationBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) {
    fun showExpiryNotification(medication: MedicationEntity) {
        // Intent for "Mark as Removed from Stock"
        val markRemovedIntent = Intent(context, ExpiryActionReceiver::class.java).apply {
            action = NotificationConstants.ACTION_MARK_REMOVED
            putExtra(NotificationConstants.EXTRA_MEDICATION_ID, medication.id)
        }
        val markRemovedPendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.toInt(),
            markRemovedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent for "Remind Later"
        val remindLaterIntent = Intent(context, ExpiryActionReceiver::class.java).apply {
            action = NotificationConstants.ACTION_REMIND_LATER
            putExtra(NotificationConstants.EXTRA_MEDICATION_ID, medication.id)
        }
        val remindLaterPendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.toInt() + 100000,
            remindLaterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val isExpired = medication.expiryDate?.let { it < System.currentTimeMillis() } ?: false
        val title = if (isExpired) "Medication Expired!" else "Medication Expiring Soon"
        val text = if (isExpired) {
            "${medication.name} (${medication.dosageStrength}) has expired. Please remove it from your stock."
        } else {
            "${medication.name} (${medication.dosageStrength}) is expiring soon. Please check your stock."
        }

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID_EXPIRY_ALERTS)
            // Use standard alert icon; you can replace it with R.drawable.ic_medyo_logo later
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_delete,
                "Removed from Stock",
                markRemovedPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_recent_history,
                "Remind Later",
                remindLaterPendingIntent
            )
            .build()

        // Use medication ID as the notification ID so multiple meds get distinct notifications
        notificationManager.notify(
            NotificationConstants.NOTIFICATION_ID_EXPIRY_BASE + medication.id.toInt(),
            notification
        )
    }
}

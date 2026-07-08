package medyo.com.core.notification.impl

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.notification.NotificationConstants
import medyo.com.core.notification.R
import medyo.com.core.notification.api.Notifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationChannelManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager
) : Notifier {

    fun create() {
        ensureNotificationChannelExists()
    }

    override fun notifyExpiry(medications: List<MedicationInfo>) {
        TODO("Not yet implemented")
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun notifyDosage(medications: List<MedicationInfo>) {
//
//        getString(
//            R.string.core_notifications_news_notification_group_summary,
//            truncatedNewsResources.size,
//        )
        val dosagesNotification = createDosageNotification {
            setContentTitle("Dosage Reminders")
                .setContentText("Dosage Reminders")
                .setSmallIcon(R.drawable.ic_notification)
                // Build summary info into InboxStyle template.
                .setStyle(newsNotificationStyle(medications, "Dosage Reminders"))
                .setGroup(NotificationConstants.NOTIFICATION_DOSAGE_GROUP)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build()
        }

        notificationManager.notify(
            NotificationConstants.NOTIFICATION_ID_EXPIRY_BASE,
            dosagesNotification
        )
    }

    /**
     * Creates an inbox style summary notification for news updates
     */
    private fun newsNotificationStyle(
        medResources: List<MedicationInfo>,
        title: String,
    ): InboxStyle = medResources
        .fold(InboxStyle()) { inboxStyle, newsResource -> inboxStyle.addLine(newsResource.name) }
        .setBigContentTitle(title)
        .setSummaryText(title)

    private fun createDosageNotification(
        block: NotificationCompat.Builder.() -> Unit,
    ): Notification {
        ensureNotificationChannelExists()
        return NotificationCompat.Builder(
            context,
            NotificationConstants.CHANNEL_ID_DOSAGE_ALERTS,
        )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .apply(block)
            .build()
    }

    private fun createExpiryNotification(
        block: NotificationCompat.Builder.() -> Unit,
    ): Notification {
        ensureNotificationChannelExists()
        return NotificationCompat.Builder(
            context,
            NotificationConstants.CHANNEL_ID_EXPIRY_ALERTS,
        ).setPriority(NotificationCompat.PRIORITY_MAX)
            .apply(block)
            .build()
    }

    /**
     * Ensures that a notification channel is present if applicable
     */
    private fun ensureNotificationChannelExists() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val dosageChannel = createDosageChannel()
        val expiryChannel = createExpiryChannel()

        // Register the channel with the system
        notificationManager.createNotificationChannels(listOf(dosageChannel, expiryChannel))
    }

    private fun createExpiryChannel() = NotificationChannel(
        NotificationConstants.CHANNEL_ID_EXPIRY_ALERTS,
        "Medication Expiry Alerts",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description =
            "medications in your stock are about to expire or have expired."
    }

    private fun createDosageChannel() = NotificationChannel(
        NotificationConstants.CHANNEL_ID_DOSAGE_ALERTS,
        "Dosage Reminders",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Reminders to take your scheduled medications."
        setBypassDnd(true)
    }

    override fun requestPermissionIfNeeded(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NotificationConstants.NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

}
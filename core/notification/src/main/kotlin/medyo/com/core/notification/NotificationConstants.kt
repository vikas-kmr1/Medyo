package medyo.com.core.notification

object NotificationConstants {
    const val NOTIFICATION_PERMISSION_CODE = 1001
    // Channel IDs
    const val CHANNEL_ID_EXPIRY_ALERTS = "medyo_expiry_alerts_channel"
    const val CHANNEL_ID_DOSAGE_ALERTS = "medyo_dosage_alerts_channel"
    
    // Notification IDs
    const val NOTIFICATION_ID_EXPIRY_BASE = 1000
    const val NOTIFICATION_ID_DOSAGE_BASE = 2000

const val NOTIFICATION_EXPIRY_GROUP = "NOTIFICATION_EXPIRY_GROUP"
    const val NOTIFICATION_DOSAGE_GROUP = "NOTIFICATION_DOSAGE_GROUP"

    // Intent Actions
    const val ACTION_MARK_REMOVED = "medyo.action.MARK_REMOVED"
    const val ACTION_REMIND_LATER = "medyo.action.REMIND_LATER"

    // Intent Extras
    const val EXTRA_MEDICATION_ID = "extra_medication_id"
}

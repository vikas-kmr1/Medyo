package medyo.com.core.expiryalert

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import medyo.com.core.database.dao.MedicationDao
import javax.inject.Inject

@AndroidEntryPoint
class ExpiryActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var medicationDao: MedicationDao

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {/*
        val medicationId = intent.getLongExtra(NotificationConstants.EXTRA_MEDICATION_ID, -1L)
        if (medicationId == -1L) return

        val notificationId = NotificationConstants.NOTIFICATION_ID_EXPIRY_BASE + medicationId.toInt()

        when (intent.action) {
            NotificationConstants.ACTION_MARK_REMOVED -> {
                // Launch coroutine to update DB
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        medicationDao.markAsRemovedFromStock(medicationId)
                    } finally {
                        pendingResult.finish()
                    }
                }
                // Dismiss notification
                notificationManager.cancel(notificationId)
            }
            NotificationConstants.ACTION_REMIND_LATER -> {
                // Just dismiss the notification for now.
                // The WorkManager will pick it up again on the next 12h run.
                notificationManager.cancel(notificationId)
            }
        }*/
    }
}

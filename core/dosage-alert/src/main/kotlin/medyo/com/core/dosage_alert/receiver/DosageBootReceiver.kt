package medyo.com.core.dosage_alert.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import medyo.com.core.dosage_alert.usecase.RescheduleAllAlarmsUseCase
import javax.inject.Inject

@AndroidEntryPoint
class DosageBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var rescheduleAllAlarmsUseCase: RescheduleAllAlarmsUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || 
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            
            val pendingResult = goAsync()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    rescheduleAllAlarmsUseCase()
                    Log.d("DosageBootReceiver", "Successfully rescheduled all alarms on boot/update")
                } catch (e: Exception) {
                    Log.e("DosageBootReceiver", "Failed to reschedule alarms", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

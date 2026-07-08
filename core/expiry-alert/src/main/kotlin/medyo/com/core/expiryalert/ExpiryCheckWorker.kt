package medyo.com.core.expiryalert

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import medyo.com.core.datastore.MedyoPreferences
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@HiltWorker
class ExpiryCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getExpiringMedicationsUseCase: GetExpiringMedicationsUseCase,
    private val notificationBuilder: ExpiryNotificationBuilder,
    private val medyoPreferences: MedyoPreferences
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val morningTimeString = medyoPreferences.expiryMorningTime.first()
            val eveningTimeString = medyoPreferences.expiryEveningTime.first()
            
            val morningTime = LocalTime.parse(morningTimeString, DateTimeFormatter.ofPattern("HH:mm"))
            val eveningTime = LocalTime.parse(eveningTimeString, DateTimeFormatter.ofPattern("HH:mm"))
            
            val currentTime = LocalTime.now()
            
            val isMorningSlot = isWithin30Minutes(currentTime, morningTime)
            val isEveningSlot = isWithin30Minutes(currentTime, eveningTime)
            
            if (!isMorningSlot && !isEveningSlot) {
                return Result.success()
            }
            
            val now = System.currentTimeMillis()
            val expiringMedications = getExpiringMedicationsUseCase(now)
            
            expiringMedications.forEach { medication ->
                // Basic logic: if evening slot, only notify for <= 3 days remaining.
                val daysRemaining = ((medication.expiryDate ?: 0) - now) / (1000 * 60 * 60 * 24)
                if (isEveningSlot && daysRemaining > 3) {
                    return@forEach
                }
                
                notificationBuilder.showExpiryNotification(medication)
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun isWithin30Minutes(time1: LocalTime, time2: LocalTime): Boolean {
        val diffMinutes = Math.abs(java.time.Duration.between(time1, time2).toMinutes())
        return diffMinutes <= 30
    }
}

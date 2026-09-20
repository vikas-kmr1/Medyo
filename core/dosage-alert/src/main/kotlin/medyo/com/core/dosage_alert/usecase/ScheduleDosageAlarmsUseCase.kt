package medyo.com.core.dosage_alert.usecase

import javax.inject.Inject
import medyo.com.core.database.dao.DosageAlertDao
import medyo.com.core.database.entity.ScheduleEntity
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.util.NextAlarmCalculator

class ScheduleDosageAlarmsUseCase @Inject constructor(
    private val dosageAlertDao: DosageAlertDao,
    private val dosageAlarmScheduler: DosageAlarmScheduler,
) {
    suspend operator fun invoke(
        medicationId: Long,
        dosageTimes: List<String>,
        startDate: Long,
        endDate: Long?,
        frequency: String = "DAILY"
    ): Result<Unit> {
        return try {
            dosageAlertDao.deleteSchedulesForMedication(medicationId)
            
            for (timeOfDay in dosageTimes) {
                val schedule = ScheduleEntity(
                    medicationId = medicationId,
                    timeOfDay = timeOfDay,
                    startDate = startDate,
                    endDate = endDate,
                    frequency = frequency
                )
                
                val scheduleId = dosageAlertDao.insertSchedule(schedule)
                
                val nextAlarm = NextAlarmCalculator.computeNextAlarmMillis(
                    timeOfDay = timeOfDay,
                    startDate = startDate,
                    endDate = endDate,
                    frequency = frequency
                )
                
                if (nextAlarm != null) {
                    dosageAlarmScheduler.scheduleDosageAlarm(scheduleId, medicationId, nextAlarm)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

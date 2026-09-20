package medyo.com.core.dosage_alert.usecase

import javax.inject.Inject
import medyo.com.core.database.dao.DosageAlertDao
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.util.NextAlarmCalculator

class RescheduleAllAlarmsUseCase @Inject constructor(
    private val dosageAlertDao: DosageAlertDao,
    private val dosageAlarmScheduler: DosageAlarmScheduler,
) {
    suspend operator fun invoke() {
        val nowSeconds = System.currentTimeMillis() / 1000
        val activeSchedules = dosageAlertDao.getAllActiveSchedules(nowSeconds)
        
        for (schedule in activeSchedules) {
            val nextAlarm = NextAlarmCalculator.computeNextAlarmMillis(
                timeOfDay = schedule.timeOfDay,
                startDate = schedule.startDate,
                endDate = schedule.endDate,
                frequency = schedule.frequency
            )
            
            if (nextAlarm != null) {
                dosageAlarmScheduler.scheduleDosageAlarm(schedule.id, schedule.medicationId, nextAlarm)
            }
        }
    }
}

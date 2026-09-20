package medyo.com.core.dosage_alert.usecase

import java.time.Instant
import javax.inject.Inject
import medyo.com.core.database.dao.DosageAlertDao
import medyo.com.core.database.entity.DosageHistoryEntity
import medyo.com.core.database.entity.DosageStatus
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.util.NextAlarmCalculator

enum class DosageAction { TAKE, SKIP, SNOOZE }

class RecordDosageActionUseCase @Inject constructor(
    private val dosageAlertDao: DosageAlertDao,
    private val dosageAlarmScheduler: DosageAlarmScheduler,
) {
    suspend operator fun invoke(
        action: DosageAction,
        scheduleId: Long,
        medicationId: Long,
        scheduledTimestamp: Long,
        historyId: Long? = null,
    ) {
        when (action) {
            DosageAction.TAKE -> {
                val now = Instant.now().toEpochMilli()
                if (historyId != null) {
                    dosageAlertDao.updateDosageHistoryStatus(historyId, DosageStatus.TAKEN, now)
                } else {
                    val history = DosageHistoryEntity(
                        medicationId = medicationId,
                        scheduleId = scheduleId,
                        scheduledTimestamp = scheduledTimestamp,
                        actualTakenTimestamp = now,
                        status = DosageStatus.TAKEN
                    )
                    dosageAlertDao.insertDosageHistory(history)
                }
                scheduleNextAlarm(scheduleId)
            }
            DosageAction.SKIP -> {
                if (historyId != null) {
                    dosageAlertDao.updateDosageHistoryStatus(historyId, DosageStatus.SKIPPED, null)
                } else {
                    val history = DosageHistoryEntity(
                        medicationId = medicationId,
                        scheduleId = scheduleId,
                        scheduledTimestamp = scheduledTimestamp,
                        actualTakenTimestamp = null,
                        status = DosageStatus.SKIPPED
                    )
                    dosageAlertDao.insertDosageHistory(history)
                }
                scheduleNextAlarm(scheduleId)
            }
            DosageAction.SNOOZE -> {
                val snoozeTime = System.currentTimeMillis() + 15 * 60 * 1000
                dosageAlarmScheduler.scheduleDosageAlarm(scheduleId, medicationId, snoozeTime)
            }
        }
    }

    private suspend fun scheduleNextAlarm(scheduleId: Long) {
        val schedule = dosageAlertDao.getScheduleById(scheduleId)
        if (schedule != null) {
            val nextAlarm = NextAlarmCalculator.computeNextAlarmMillis(
                timeOfDay = schedule.timeOfDay,
                startDate = schedule.startDate,
                endDate = schedule.endDate,
                frequency = schedule.frequency
            )
            if (nextAlarm != null) {
                dosageAlarmScheduler.scheduleDosageAlarm(scheduleId, schedule.medicationId, nextAlarm)
            }
        }
    }
}

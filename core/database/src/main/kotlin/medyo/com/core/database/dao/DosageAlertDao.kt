package medyo.com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import medyo.com.core.database.entity.DosageHistoryEntity
import medyo.com.core.database.entity.ScheduleEntity
import medyo.com.core.database.entity.DosageStatus

@Dao
interface DosageAlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    @Query("SELECT * FROM schedules WHERE medicationId = :medicationId")
    fun getSchedulesForMedication(medicationId: Long): Flow<List<ScheduleEntity>>

    @Query("DELETE FROM schedules WHERE medicationId = :medicationId")
    suspend fun deleteSchedulesForMedication(medicationId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDosageHistory(history: DosageHistoryEntity): Long

    @Query("UPDATE dosage_history SET status = :status, actualTakenTimestamp = :timestamp WHERE id = :historyId")
    suspend fun updateDosageHistoryStatus(historyId: Long, status: DosageStatus, timestamp: Long?)

    @Query("SELECT * FROM dosage_history WHERE medicationId = :medicationId ORDER BY scheduledTimestamp DESC")
    fun getDosageHistoryForMedication(medicationId: Long): Flow<List<DosageHistoryEntity>>

    @Query("SELECT * FROM dosage_history WHERE scheduledTimestamp >= :startTime AND scheduledTimestamp <= :endTime ORDER BY scheduledTimestamp ASC")
    fun getDosageHistoryForDateRange(startTime: Long, endTime: Long): Flow<List<DosageHistoryEntity>>
    
    @Query("SELECT * FROM dosage_history WHERE status = :status AND scheduledTimestamp < :now")
    suspend fun getOverdueDosages(status: DosageStatus, now: Long): List<DosageHistoryEntity>

    @Query("SELECT * FROM schedules WHERE endDate IS NULL OR endDate >= :now")
    suspend fun getAllActiveSchedules(now: Long): List<ScheduleEntity>

    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    suspend fun getScheduleById(scheduleId: Long): ScheduleEntity?

    @Query("SELECT * FROM dosage_history WHERE id = :historyId")
    suspend fun getDosageHistoryById(historyId: Long): DosageHistoryEntity?
}

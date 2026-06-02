package medyo.com.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dosage_history",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.SET_NULL // Keep history even if schedule is deleted
        )
    ],
    indices = [Index("medicationId"), Index("scheduleId")]
)
data class DosageHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val scheduleId: Long?,
    val scheduledTimestamp: Long, // When it was supposed to be taken
    val actualTakenTimestamp: Long?, // When the user actually tapped "Log Intake"
    val status: DosageStatus
)
package medyo.com.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId")]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val timeOfDay: String, // Format "HH:mm" (e.g., "08:00")
    val frequency: String, // e.g., "DAILY", "WEEKLY", "CUSTOM"
    val startDate: Long,
    val endDate: Long? // Null for continuous prescriptions
)
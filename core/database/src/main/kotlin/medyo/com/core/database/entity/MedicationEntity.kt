package medyo.com.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MedicationCategory {
    RUNNING_DOSE,   // Needs scheduling
    FIRST_AID_STOCK // Expiry tracking only
}

enum class DosageStatus {
    PENDING, TAKEN, SKIPPED, MISSED
}

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosageStrength: String, // e.g., "500mg"
    val form: String, // e.g., "Tablet", "Syrup"
    val category: MedicationCategory,
    val stockQuantity: Int, // Decrements when a dose is logged
    val expiryDate: Long?, // Epoch timestamp
    val alertDaysBeforeExpiry: Int? // e.g., Notify 30 days before
)
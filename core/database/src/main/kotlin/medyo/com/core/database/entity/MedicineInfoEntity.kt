package medyo.com.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicine_info",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE // Deletes info if the core medication is deleted
        )
    ]
)
data class MedicineInfoEntity(
    @PrimaryKey val medicationId: Long, // Acts as both PK and FK
    val brand: String?,
    val salts: String?,
    val sideEffects: List<String> = emptyList(),
    val cures: List<String> = emptyList(),
    val precautions: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val mfgDate: Long? = null,
    val errorMessage: String? = null,
    val statusCode: String? = null
)
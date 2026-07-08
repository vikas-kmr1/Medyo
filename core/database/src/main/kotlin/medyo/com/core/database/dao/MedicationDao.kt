package medyo.com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicationInfoEntity

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationInfo(MedicationInfo: MedicationInfoEntity)

    // Fetches the combined data using a standard SQL JOIN
    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate, m.stockQuantity, m.alertDaysBeforeExpiry,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate
        FROM medications m
        LEFT JOIN Medication_info i ON m.id = i.medicationId
        WHERE m.id = :medicationId
    """)
    fun getMedicationDetails(medicationId: Long): Flow<MedicationDetail?>

    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate,m.form,m.category, m.stockQuantity, m.alertDaysBeforeExpiry,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate
        FROM medications m
        LEFT JOIN Medication_info i ON m.id = i.medicationId
    """)
    fun getAllMedicationDetails(): Flow<List<MedicationDetail>>

    // --- Expiry Alert System Queries ---

    @Query("""
        SELECT * FROM medications 
        WHERE expiryDate IS NOT NULL 
          AND stockRemoved = 0 
          AND expiryDate > :now
          AND (expiryDate - (COALESCE(alertDaysBeforeExpiry, 7) * 86400000)) <= :now
    """)
    suspend fun getExpiringMedications(now: Long): List<MedicationEntity>

    @Query("UPDATE medications SET stockRemoved = 1 WHERE id = :medicationId")
    suspend fun markAsRemovedFromStock(medicationId: Long)

    @Query("""
        SELECT * FROM medications 
        WHERE expiryDate IS NOT NULL AND stockRemoved = 0
        ORDER BY expiryDate ASC
    """)
    fun getAllMedicationsWithExpiry(): Flow<List<MedicationEntity>>
}
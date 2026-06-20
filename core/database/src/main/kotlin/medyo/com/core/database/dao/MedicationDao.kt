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
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate
        FROM medications m
        LEFT JOIN Medication_info i ON m.id = i.medicationId
        WHERE m.id = :medicationId
    """)
    fun getMedicationDetails(medicationId: Long): Flow<MedicationDetail?>

    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate,m.form,m.category,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate
        FROM medications m
        LEFT JOIN Medication_info i ON m.id = i.medicationId
    """)
    fun getAllMedicationDetails(): Flow<List<MedicationDetail>>
}
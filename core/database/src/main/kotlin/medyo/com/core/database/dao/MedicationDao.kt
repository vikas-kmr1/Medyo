package medyo.com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicineInfoEntity

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicineInfo(medicineInfo: MedicineInfoEntity)

    // Fetches the combined data using a standard SQL JOIN
    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate, i.errorMessage, i.statusCode
        FROM medications m
        LEFT JOIN medicine_info i ON m.id = i.medicationId
        WHERE m.id = :medicationId
    """)
    fun getMedicineDetails(medicationId: Long): Flow<MedicineDetails?>

    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, m.expiryDate AS expDate,
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions, i.mfgDate, i.errorMessage, i.statusCode
        FROM medications m
        LEFT JOIN medicine_info i ON m.id = i.medicationId
    """)
    fun getAllMedicineDetails(): Flow<List<MedicineDetails>>
}
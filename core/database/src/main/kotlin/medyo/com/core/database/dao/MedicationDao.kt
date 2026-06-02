package medyo.com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    // Fetches the combined data using a standard SQL JOIN
    @Query("""
        SELECT m.id AS medicationId, m.name, m.dosageStrength, 
               i.brand, i.salts, i.sideEffects, i.cures, i.precautions, i.instructions
        FROM medications m
        LEFT JOIN medicine_info i ON m.id = i.medicationId
        WHERE m.id = :medicationId
    """)
    fun getMedicineDetails(medicationId: Long): Flow<MedicineDetailsDto>
}
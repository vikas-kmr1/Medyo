package medyo.com.core.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import medyo.com.core.domain.model.MedicineInfo

interface BioScanRepository {
    /**
     * Sends images to the AI logic for context generation.
     * If successful, saves the generated information to the database and returns the generated medicationId.
     */
    suspend fun scanAndSaveMedicine(images: List<Bitmap>): Result<Long>

    /**
     * Reads a scanned medicine from the local database, returning a continuous stream (Flow) 
     * mapped to the domain model `MedicineInfo`.
     */
    fun getScannedMedicine(medicationId: Long): Flow<MedicineInfo?>
}

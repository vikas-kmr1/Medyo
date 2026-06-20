package medyo.com.core.domain.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import medyo.com.core.domain.model.MedicationInfo

interface BioScanRepository {
    /**
     * Sends images to the AI logic for context generation.
     * If successful, saves the generated information to the database and returns the generated medicationId.
     */
    suspend fun scanAndGetMedication(images: List<Bitmap>): Result<MedicationInfo>

    suspend fun saveMedication(medicationInfo: MedicationInfo): Result<Long>

    /**
     * Reads a scanned Medication from the local database, returning a continuous stream (Flow) 
     * mapped to the domain model `MedicationInfo`.
     */
    fun getScannedMedication(medicationId: Long): Flow<MedicationInfo?>

    /**
     * Reads all scanned Medications from the local database.
     */
    fun getAllScannedMedications(): Flow<List<MedicationInfo>>
}

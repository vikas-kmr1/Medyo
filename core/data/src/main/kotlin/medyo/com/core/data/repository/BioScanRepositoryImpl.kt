package medyo.com.core.data.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import medyo.com.core.ai_logic.GeminiAiDataSource
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.entity.MedicationCategory
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicineInfoEntity
import medyo.com.core.domain.model.MedicineInfo
import medyo.com.core.domain.repository.BioScanRepository
import medyo.com.core.data.mapper.toMedicationEntity
import medyo.com.core.data.mapper.toMedicineInfoEntity
import medyo.com.core.data.mapper.toDomainModel
import javax.inject.Inject

class BioScanRepositoryImpl @Inject constructor(
    private val aiDataSource: GeminiAiDataSource,
    private val medicationDao: MedicationDao
) : BioScanRepository {

    override suspend fun scanAndSaveMedicine(images: List<Bitmap>): Result<Long> {
        return try {
            val aiResponse = aiDataSource.generateContext(images)
                ?: return Result.failure(Exception("AI returned empty response"))

            if (!aiResponse.errorMessage.isNullOrBlank()) {
                return Result.failure(Exception(aiResponse.errorMessage))
            }

            // 1. Create and insert base MedicationEntity
            val medicationEntity = aiResponse.toMedicationEntity()
            val generatedId = medicationDao.insertMedication(medicationEntity)

            // 2. Create and insert detailed MedicineInfoEntity
            val infoEntity = aiResponse.toMedicineInfoEntity(generatedId)
            medicationDao.insertMedicineInfo(infoEntity)

            Result.success(generatedId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getScannedMedicine(medicationId: Long): Flow<MedicineInfo?> {
        return medicationDao.getMedicineDetails(medicationId).map { details ->
            details?.toDomainModel()
        }
    }
}

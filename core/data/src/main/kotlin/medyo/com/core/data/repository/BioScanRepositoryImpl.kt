package medyo.com.core.data.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import medyo.com.core.ai_logic.GeminiAiDataSource
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.repository.BioScanRepository
import medyo.com.core.data.mapper.toMedicationEntity
import medyo.com.core.data.mapper.toMedicationInfoEntity
import medyo.com.core.data.mapper.toDomainModel
import javax.inject.Inject

class BioScanRepositoryImpl @Inject constructor(
    private val aiDataSource: GeminiAiDataSource,
    private val medicationDao: MedicationDao
) : BioScanRepository {

    override suspend fun scanAndSaveMedication(images: List<Bitmap>): Result<Long> {
        return try {
            val aiResponse = aiDataSource.generateContext(images)
                ?: return Result.failure(Exception("AI returned empty response"))

            if (!aiResponse.errorMessage.isNullOrBlank()) {
                return Result.failure(Exception(aiResponse.errorMessage))
            }

            // 1. Create and insert base MedicationEntity
            val medicationEntity = aiResponse.toMedicationEntity()
            val generatedId = medicationDao.insertMedication(medicationEntity)

            // 2. Create and insert detailed MedicationInfoEntity
            val infoEntity = aiResponse.toMedicationInfoEntity(generatedId)
            medicationDao.insertMedicationInfo(infoEntity)

            Result.success(generatedId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getScannedMedication(medicationId: Long): Flow<MedicationInfo?> {
        return medicationDao.getMedicationDetails(medicationId).map { details ->
            details?.toDomainModel()
        }
    }

    override fun getAllScannedMedications(): Flow<List<MedicationInfo>> {
        return medicationDao.getAllMedicationDetails().map { detailsList ->
            detailsList.map { it.toDomainModel() }
        }
    }
}

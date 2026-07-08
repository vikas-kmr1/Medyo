package medyo.com.core.expiryalert

import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.entity.MedicationEntity
import javax.inject.Inject

class GetExpiringMedicationsUseCase @Inject constructor(
    private val medicationDao: MedicationDao
) {
    suspend operator fun invoke(nowTimeMillis: Long): List<MedicationEntity> {
        return medicationDao.getExpiringMedications(nowTimeMillis)
    }
}

package medyo.com.core.expiryalert

import kotlinx.coroutines.flow.Flow
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.entity.MedicationEntity
import javax.inject.Inject

class GetAllMedicationsWithExpiryUseCase @Inject constructor(
    private val medicationDao: MedicationDao
) {
    operator fun invoke(): Flow<List<MedicationEntity>> {
        return medicationDao.getAllMedicationsWithExpiry()
    }
}

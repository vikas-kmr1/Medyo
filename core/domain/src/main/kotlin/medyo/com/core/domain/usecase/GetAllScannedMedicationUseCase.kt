package medyo.com.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.repository.BioScanRepository
import javax.inject.Inject

class GetAllScannedMedicationsUseCase @Inject constructor(
    private val repository: BioScanRepository
) {
    operator fun invoke(): Flow<List<MedicationInfo>> {
        return repository.getAllScannedMedications()
    }
}

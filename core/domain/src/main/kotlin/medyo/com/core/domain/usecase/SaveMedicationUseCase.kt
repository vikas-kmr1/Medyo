package medyo.com.core.domain.usecase

import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.repository.BioScanRepository
import javax.inject.Inject

class SaveMedicationUseCase@Inject constructor(
    private val repository: BioScanRepository
)  {
    suspend operator fun invoke(medicationInfo: MedicationInfo){
        repository.saveMedication(medicationInfo = medicationInfo)
    }
}
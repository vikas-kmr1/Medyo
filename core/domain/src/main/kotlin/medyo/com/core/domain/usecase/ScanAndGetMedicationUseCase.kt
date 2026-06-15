package medyo.com.core.domain.usecase

import android.graphics.Bitmap
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.repository.BioScanRepository
import javax.inject.Inject

class ScanAndGetMedicationUseCase @Inject constructor(
    private val repository: BioScanRepository
) {
    suspend operator fun invoke(images: List<Bitmap>): Result<MedicationInfo> {
        return repository.scanAndGetMedication(images)
    }
}

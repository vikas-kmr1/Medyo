package medyo.com.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import medyo.com.core.domain.model.MedicineInfo
import medyo.com.core.domain.repository.BioScanRepository
import javax.inject.Inject

class GetAllScannedMedicinesUseCase @Inject constructor(
    private val repository: BioScanRepository
) {
    operator fun invoke(): Flow<List<MedicineInfo>> {
        return repository.getAllScannedMedicines()
    }
}

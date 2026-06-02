package medyo.com.bio_scan.impl

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import medyo.com.core.domain.model.MedicineInfo
import medyo.com.core.domain.usecase.GetAllScannedMedicinesUseCase
import medyo.com.core.domain.usecase.GetScannedMedicineUseCase
import medyo.com.core.domain.usecase.ScanAndSaveMedicineUseCase
import javax.inject.Inject

sealed interface BioScanUiState {
    data object Idle : BioScanUiState
    data object Loading : BioScanUiState
    data class Success(val medicineInfo: MedicineInfo) : BioScanUiState
    data class Error(val message: String) : BioScanUiState
}

@HiltViewModel
class BioScanViewmodel @Inject constructor(
    private val scanAndSaveMedicineUseCase: ScanAndSaveMedicineUseCase,
    private val getScannedMedicineUseCase: GetScannedMedicineUseCase,
    getAllScannedMedicinesUseCase: GetAllScannedMedicinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BioScanUiState>(BioScanUiState.Idle)
    val uiState: StateFlow<BioScanUiState> = _uiState.asStateFlow()

    val scannedMedicinesList: StateFlow<List<MedicineInfo>> = getAllScannedMedicinesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


}
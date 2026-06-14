package medyo.com.bio_scan.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.usecase.GetAllScannedMedicationsUseCase
import medyo.com.core.domain.usecase.GetScannedMedicationUseCase
import medyo.com.core.domain.usecase.ScanAndSaveMedicationUseCase
import javax.inject.Inject

sealed interface BioScanUiState {
    data object Idle : BioScanUiState
    data object Loading : BioScanUiState
    data class Success(val medicationInfo: MedicationInfo) : BioScanUiState
    data class Error(val message: String) : BioScanUiState
}

@HiltViewModel
class BioScanViewmodel @Inject constructor(
    private val scanAndSaveMedicationUseCase: ScanAndSaveMedicationUseCase,
    private val getScannedMedicationUseCase: GetScannedMedicationUseCase,
    getAllScannedMedicationsUseCase: GetAllScannedMedicationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BioScanUiState>(BioScanUiState.Idle)
    val uiState: StateFlow<BioScanUiState> = _uiState.asStateFlow()

    val scannedMedicationsList: StateFlow<List<MedicationInfo>> = getAllScannedMedicationsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


}
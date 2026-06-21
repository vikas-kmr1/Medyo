package medyo.com.feature.detail.impl

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.usecase.GetScannedMedicationUseCase
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val getScannedMedicationUseCase: GetScannedMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MedicationDetailUiState>(MedicationDetailUiState.Loading)
    val uiState: StateFlow<MedicationDetailUiState> = _uiState.asStateFlow()

    fun getMedicationDetail(medicationId: Long) {
        viewModelScope.launch {
            _uiState.value = MedicationDetailUiState.Loading
            getScannedMedicationUseCase(medicationId).collect { medicationInfo ->
                if (medicationInfo != null) {
                    _uiState.value = MedicationDetailUiState.Success(medicationInfo)
                } else {
                    _uiState.value = MedicationDetailUiState.Error("Medication not found")
                }
            }
        }
    }
}

sealed interface MedicationDetailUiState {
    data object Loading : MedicationDetailUiState
    data class Success(val medicationInfo: MedicationInfo) : MedicationDetailUiState
    data class Error(val message: String) : MedicationDetailUiState
}
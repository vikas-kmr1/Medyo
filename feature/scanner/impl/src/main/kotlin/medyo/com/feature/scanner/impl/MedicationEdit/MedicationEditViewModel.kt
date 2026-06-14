package medyo.com.feature.scanner.impl.MedicationEdit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import medyo.com.core.utils.constants.MedicationCategory
import medyo.com.core.utils.constants.MedicationType
import medyo.com.core.utils.kotlin.emptyString
import javax.inject.Inject

data class MedicationEditUiState(
    val name: String = emptyString,
    val manufacturer: String = emptyString,
    val medicationType: MedicationType = MedicationType.CAPSULE,
    val category: MedicationCategory = MedicationCategory.FIRST_AID_STOCK,
    val manufacturingDate: String = emptyString,
    val expiryDate: String = emptyString,
    val dosageIntervalMinutes: String = emptyString,
    val startDate: String = emptyString,
    val endDate: String = emptyString,
    val totalDoses: String = emptyString,
    val isLoading: Boolean = false
)

@HiltViewModel
class MedicationEditViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MedicationEditUiState())
    val uiState: StateFlow<MedicationEditUiState> = _uiState.asStateFlow()

    fun onInit(medicationEditUiState: MedicationEditUiState) {
        _uiState.value = medicationEditUiState
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onManufacturerChange(manufacturer: String) {
        _uiState.update { it.copy(manufacturer = manufacturer) }
    }

    fun onMedicationTypeChange(type: MedicationType) {
        _uiState.update { it.copy(medicationType = type) }
    }

    fun onCategoryChange(category: MedicationCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun onManufacturingDateChange(date: String) {
        _uiState.update { it.copy(manufacturingDate = date) }
    }

    fun onExpiryDateChange(date: String) {
        _uiState.update { it.copy(expiryDate = date) }
    }

    fun onDosageIntervalChange(interval: String) {
        _uiState.update { it.copy(dosageIntervalMinutes = interval) }
    }

    fun onStartDateChange(date: String) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun onEndDateChange(date: String) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun onTotalDosesChange(totalDoses: String) {
        _uiState.update { it.copy(totalDoses = totalDoses) }
    }

    fun onSave() {
        // Implement save logic later
    }
}

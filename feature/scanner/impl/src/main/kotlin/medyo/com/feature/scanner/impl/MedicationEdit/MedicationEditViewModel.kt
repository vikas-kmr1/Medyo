package medyo.com.feature.scanner.impl.MedicationEdit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.usecase.SaveMedicationUseCase
import medyo.com.core.utils.constants.MedicationCategory
import medyo.com.core.utils.constants.MedicationType
import medyo.com.core.utils.kotlin.emptyString
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class MedicationEditUiState(
    val name: String = "",
    val nameError: String? = null,
    val manufacturer: String = "",
    val medicationType: MedicationType = MedicationType.OTHER,
    val category: MedicationCategory = MedicationCategory.FIRST_AID_STOCK,
    val manufacturingDate: LocalDate? = null,
    val manufacturingDateError: String? = null,
    val expiryDate: LocalDate? = null,
    val expiryDateError: String? = null,
    val dosageIntervalMinutes: String = "",
    val dosageIntervalError: String? = null,
    val startDate: LocalDate? = null,
    val startDateError: String? = null,
    val endDate: LocalDate? = null,
    val endDateError: String? = null,
    val totalDoses: String = "",
    val totalDosesError: String? = null,
    val isLoading: Boolean = false,
    val sideEffects:List<String> = emptyList(),
    val cures:List<String> = emptyList(),
    val precautions:List<String> = emptyList(),
    val instructions:List<String> = emptyList(),
)

@HiltViewModel
class MedicationEditViewModel @Inject constructor(
    private val saveMedicationUseCase: SaveMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicationEditUiState())
    val uiState: StateFlow<MedicationEditUiState> = _uiState.asStateFlow()


    var dbWriteState by mutableStateOf(false)

    fun onInit(medicationInfo: MedicationInfo) {
        _uiState.value = MedicationEditUiState(
            name = medicationInfo.name,
            manufacturer = medicationInfo.brand,
            category = try {
                MedicationCategory.valueOf(medicationInfo.category)
            } catch (e: Exception) {
                MedicationCategory.FIRST_AID_STOCK
            },
            manufacturingDate = medicationInfo.mfgDate?.toLocalDate(),
            expiryDate = medicationInfo.expDate?.toLocalDate(),
            dosageIntervalMinutes = medicationInfo.dosageIntervalMinutes,
            startDate = medicationInfo.startDate?.toLocalDate(),
            endDate = medicationInfo.endDate?.toLocalDate(),
            totalDoses = medicationInfo.totalDoses,
            sideEffects = medicationInfo.sideEffects,
            cures = medicationInfo.cures,
            precautions = medicationInfo.precautions,
            instructions = medicationInfo.instructions,
        )
    }

    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochSecond(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = if (name.isBlank()) "Name cannot be empty" else null
            )
        }
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

    fun onManufacturingDateChange(date: LocalDate) {
        _uiState.update {
            val expiryError = if (it.expiryDate != null && date.isAfter(it.expiryDate)) {
                "Manufacturing date must be before expiry date"
            } else null
            it.copy(
                manufacturingDate = date,
                manufacturingDateError = null,
                expiryDateError = expiryError
            )
        }
    }

    fun onExpiryDateChange(date: LocalDate) {
        _uiState.update {
            val expiryError =
                if (it.manufacturingDate != null && date.isBefore(it.manufacturingDate)) {
                    "Expiry date must be after manufacturing date"
                } else null
            it.copy(
                expiryDate = date,
                expiryDateError = expiryError
            )
        }
    }

    fun onDosageIntervalChange(interval: String) {
        val error = if (interval.isNotBlank() && interval.toIntOrNull() == null) {
            "Must be a valid number"
        } else null
        _uiState.update { it.copy(dosageIntervalMinutes = interval, dosageIntervalError = error) }
    }

    fun onStartDateChange(date: LocalDate) {
        _uiState.update {
            val endError = if (it.endDate != null && date.isAfter(it.endDate)) {
                "Start date must be before end date"
            } else null
            it.copy(
                startDate = date,
                startDateError = null,
                endDateError = endError
            )
        }
    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.update {
            val endError = if (it.startDate != null && date.isBefore(it.startDate)) {
                "End date must be after start date"
            } else null
            it.copy(
                endDate = date,
                endDateError = endError
            )
        }
    }

    fun onTotalDosesChange(totalDoses: String) {
        val error = if (totalDoses.isNotBlank() && totalDoses.toIntOrNull() == null) {
            "Must be a valid number"
        } else null
        _uiState.update { it.copy(totalDoses = totalDoses, totalDosesError = error) }
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveMedicationUseCase.invoke(medicationInfo = uiState.value.toMedicationInfo())
            result.onSuccess {medicationID->
                dbWriteState = medicationID >= 0
            }
        }
    }
}

private fun LocalDate.toEpochSecond(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
}

private fun MedicationEditUiState.toMedicationInfo(): MedicationInfo {
    return MedicationInfo(
        name = name,
        brand = manufacturer,
        category = category.name,
        mfgDate = manufacturingDate?.toEpochSecond(),
        expDate = expiryDate?.toEpochSecond(),
        dosageIntervalMinutes = dosageIntervalMinutes,
        startDate = startDate?.toEpochSecond(),
        endDate = endDate?.toEpochSecond(),
        salts = emptyString,
        form = medicationType.name,
        sideEffects = sideEffects,
        cures = cures,
        precautions = precautions,
        instructions =instructions,
        totalDoses = totalDoses,)
}



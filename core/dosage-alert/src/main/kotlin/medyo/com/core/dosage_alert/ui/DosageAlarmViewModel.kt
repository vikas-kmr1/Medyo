package medyo.com.core.dosage_alert.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import medyo.com.core.dosage_alert.usecase.DosageAction
import medyo.com.core.domain.usecase.GetScannedMedicationUseCase
import medyo.com.core.dosage_alert.usecase.RecordDosageActionUseCase
import medyo.com.core.dosage_alert.scheduler.DosageAlarmSchedulerImpl
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class DosageAlarmUiState(
    val medicationName: String = "",
    val scheduledTimeFormatted: String = "",
    val isLoading: Boolean = false,
)

@HiltViewModel
class DosageAlarmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getScannedMedicationUseCase: GetScannedMedicationUseCase,
    private val recordDosageActionUseCase: RecordDosageActionUseCase,
) : ViewModel() {

    private val scheduleId: Long = savedStateHandle.get<Long>(DosageAlarmSchedulerImpl.EXTRA_SCHEDULE_ID) ?: -1L
    private val medicationId: Long = savedStateHandle.get<Long>(DosageAlarmSchedulerImpl.EXTRA_MEDICATION_ID) ?: -1L
    private val scheduledTimestamp: Long = savedStateHandle.get<Long>(DosageAlarmSchedulerImpl.EXTRA_SCHEDULED_TIMESTAMP) ?: -1L

    private val _uiState = MutableStateFlow(DosageAlarmUiState())
    val uiState: StateFlow<DosageAlarmUiState> = _uiState.asStateFlow()

    init {
        loadMedication()
        formatScheduledTime()
    }

    private fun loadMedication() {
        if (medicationId == -1L) return
        viewModelScope.launch {
            getScannedMedicationUseCase(medicationId).collect { info ->
                if (info != null) {
                    _uiState.update { it.copy(medicationName = info.name) }
                }
            }
        }
    }

    private fun formatScheduledTime() {
        if (scheduledTimestamp > 0) {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())
            val timeString = formatter.format(Instant.ofEpochMilli(scheduledTimestamp))
            _uiState.update { it.copy(scheduledTimeFormatted = timeString) }
        }
    }

    fun onTake() {
        viewModelScope.launch(Dispatchers.IO) {
            recordDosageActionUseCase(
                action = DosageAction.TAKE,
                scheduleId = scheduleId,
                medicationId = medicationId,
                scheduledTimestamp = scheduledTimestamp,
            )
        }
    }

    fun onSkip() {
        viewModelScope.launch(Dispatchers.IO) {
            recordDosageActionUseCase(
                action = DosageAction.SKIP,
                scheduleId = scheduleId,
                medicationId = medicationId,
                scheduledTimestamp = scheduledTimestamp,
            )
        }
    }

    fun onSnooze() {
        viewModelScope.launch(Dispatchers.IO) {
            recordDosageActionUseCase(
                action = DosageAction.SNOOZE,
                scheduleId = scheduleId,
                medicationId = medicationId,
                scheduledTimestamp = scheduledTimestamp,
            )
        }
    }
}

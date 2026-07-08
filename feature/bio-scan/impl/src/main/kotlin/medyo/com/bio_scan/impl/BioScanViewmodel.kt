package medyo.com.bio_scan.impl

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.domain.usecase.GetAllScannedMedicationsUseCase
import medyo.com.core.domain.usecase.GetScannedMedicationUseCase
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.scheduler.DosageAlarmSchedulerImpl
import medyo.com.core.dosage_alert.ui.DosageFullScreenActivity
import medyo.com.core.notification.api.Notifier
import javax.inject.Inject

sealed interface BioScanUiState {
    data object Idle : BioScanUiState
    data object Loading : BioScanUiState
    data class Success(val medicationInfo: MedicationInfo) : BioScanUiState
    data class Error(val message: String) : BioScanUiState
}

@HiltViewModel
class BioScanViewmodel @Inject constructor(
    private val getScannedMedicationUseCase: GetScannedMedicationUseCase,
    private val getAllScannedMedicationsUseCase: GetAllScannedMedicationsUseCase,
    private val notifier: Notifier
) : ViewModel() {

    private val _uiState = MutableStateFlow<BioScanUiState>(BioScanUiState.Idle)
    val uiState: StateFlow<BioScanUiState> = _uiState.asStateFlow()

    val scannedMedicationsList: StateFlow<List<MedicationInfo>> = getAllScannedMedicationsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun notifyUser(medicationInfos: List<MedicationInfo>) {
        notifier.notifyDosage(medicationInfos)
    }

    fun notifywithFullscrenn(context: Context) {
        viewModelScope.launch {
            val intent = Intent(context, DosageFullScreenActivity::class.java)

            intent.apply {
                putExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULE_ID, 1L)
                putExtra(DosageAlarmSchedulerImpl.EXTRA_MEDICATION_ID, 1L)
                putExtra(DosageAlarmSchedulerImpl.EXTRA_SCHEDULED_TIMESTAMP, 1059686859684L)
            }
            delay(5000)
            context.startActivity(intent)
        }
    }
}
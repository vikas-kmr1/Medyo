package medyo.com.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import medyo.com.core.datastore.MedyoPreferences
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import javax.inject.Inject

data class SettingsUiState(
    val expiryMorningTime: String = "09:00",
    val expiryEveningTime: String = "18:00"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val medyoPreferences: MedyoPreferences,
    private val dosageAlarmScheduler: DosageAlarmScheduler
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        medyoPreferences.expiryMorningTime,
        medyoPreferences.expiryEveningTime
    ) { morning, evening ->
        SettingsUiState(
            expiryMorningTime = morning,
            expiryEveningTime = evening
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun updateMorningTime(time: String) {
        viewModelScope.launch {
            medyoPreferences.setExpiryMorningTime(time)
        }
    }

    fun updateEveningTime(time: String) {
        viewModelScope.launch {
            medyoPreferences.setExpiryEveningTime(time)
        }
    }
    
    fun scheduleTestAlarm() {
        dosageAlarmScheduler.scheduleDosageAlarm(
            scheduleId = 1L,
            medicationId = 1L,
            scheduledTimestamp = System.currentTimeMillis() + 60 * 1000
        )
    }
}

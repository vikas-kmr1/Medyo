package medyo.com.expiry_dashboard.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.expiryalert.GetAllMedicationsWithExpiryUseCase
import javax.inject.Inject

@HiltViewModel
class ExpiryDashboardViewModel @Inject constructor(
    getAllMedicationsWithExpiryUseCase: GetAllMedicationsWithExpiryUseCase
) : ViewModel() {

    val medicationsWithExpiry: StateFlow<List<MedicationEntity>> = getAllMedicationsWithExpiryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

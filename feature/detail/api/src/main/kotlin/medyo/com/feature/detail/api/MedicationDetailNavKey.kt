package medyo.com.feature.detail.api


import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class MedicationDetailNavKey(val medicationId: Int) : NavKey
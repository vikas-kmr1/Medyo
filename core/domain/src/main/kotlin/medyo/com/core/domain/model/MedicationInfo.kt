package medyo.com.core.domain.model

/**
 * Domain model representing the scanned and parsed Medication information.
 * This model is used by app features, abstracting away the AI/network layer DTOs.
 */
data class MedicationInfo(
    val medicationId: Long = 0,
    val name: String,
    val brand: String  = "",
    val salts: String = "",
    val mfgDate: Long? = null,
    val expDate: Long? = null,
    val sideEffects: List<String> = emptyList(),
    val cures: List<String> = emptyList(),
    val precautions: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val category: String = "",
    val form: String = "",
    val dosageIntervalMinutes: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val totalDoses: String = "",
    val errorMessage: String? = null,
    val statusCode: String? = null
)


package medyo.com.core.domain.model

/**
 * Domain model representing the scanned and parsed medicine information.
 * This model is used by app features, abstracting away the AI/network layer DTOs.
 */
data class MedicineInfo(
    val medicationId: Long,
    val brand: String = "",
    val salts: String = "",
    val mfgDate: Long? = null,
    val expDate: Long? = null,
    val sideEffects: List<String> = emptyList(),
    val cures: List<String> = emptyList(),
    val precautions: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val category: String = "",
    val errorMessage: String? = null,
    val statusCode: String? = null
)

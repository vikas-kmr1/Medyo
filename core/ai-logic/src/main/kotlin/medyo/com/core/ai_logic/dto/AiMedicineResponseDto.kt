package medyo.com.core.ai_logic.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing the structured AI response for Medications.
 * This matches the JSON schema defined in AiLogicModule.
 */
@Keep
@Serializable
data class AiMedicationResponseDto(
    val brand: String? = null,
    val salts: String? = null,
    val mfgDate: Long? = null,
    val expDate: Long? = null,
    val sideEffects: List<String> = emptyList(),
    val cures: List<String> = emptyList(),
    val precautions: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val category: String? = null,
    val errorMessage: String? = null,
    val statusCode: String? = null
)


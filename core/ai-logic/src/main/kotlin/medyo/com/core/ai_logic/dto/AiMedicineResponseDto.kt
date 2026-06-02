package medyo.com.core.ai_logic.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import medyo.com.core.domain.model.MedicineInfo
import medyo.com.core.utils.constants.MedicineType

/**
 * Data Transfer Object representing the structured AI response for medicines.
 * This matches the JSON schema defined in AiLogicModule.
 */
@Keep
@Serializable
internal data class AiMedicineResponseDto(
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
){
    /**
     * Extension function to map the AI DTO to our Domain Model.
     */
    fun AiMedicineResponseDto.toDomainModel(): MedicineInfo {
        return MedicineInfo(
            brand = this.brand.orEmpty(),
            salts = this.salts.orEmpty(),
            mfgDate = this.mfgDate,
            expDate = this.expDate,
            sideEffects = this.sideEffects,
            cures = this.cures,
            precautions = this.precautions,
            instructions = this.instructions,
            category = this.category.orEmpty(),
            errorMessage = this.errorMessage,
            statusCode = this.statusCode
        )
    }

}


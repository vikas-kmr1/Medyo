package medyo.com.core.data.mapper

import medyo.com.core.ai_logic.dto.AiMedicineResponseDto
import medyo.com.core.database.dao.MedicineDetails
import medyo.com.core.database.entity.MedicationCategory
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicineInfoEntity
import medyo.com.core.domain.model.MedicineInfo

fun AiMedicineResponseDto.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        name = this.brand ?: "Unknown Medicine",
        dosageStrength = "", // AI doesn't explicitly return this in current schema
        form = "", 
        category = MedicationCategory.FIRST_AID_STOCK, 
        stockQuantity = 0,
        expiryDate = this.expDate,
        alertDaysBeforeExpiry = null
    )
}

fun AiMedicineResponseDto.toMedicineInfoEntity(medicationId: Long): MedicineInfoEntity {
    return MedicineInfoEntity(
        medicationId = medicationId,
        brand = this.brand,
        salts = this.salts,
        sideEffects = this.sideEffects,
        cures = this.cures,
        precautions = this.precautions,
        instructions = this.instructions,
        mfgDate = this.mfgDate,
        errorMessage = null,
        statusCode = this.statusCode
    )
}

fun MedicineDetails.toDomainModel(): MedicineInfo {
    return MedicineInfo(
        medicationId = this.medicationId,
        brand = this.brand ?: this.name,
        salts = this.salts.orEmpty(),
        mfgDate = this.mfgDate,
        expDate = this.expDate,
        sideEffects = this.sideEffects,
        cures = this.cures,
        precautions = this.precautions,
        instructions = this.instructions,
        category = "", // Not currently joined or mapped directly from DB details, left blank for UI
        errorMessage = this.errorMessage,
        statusCode = this.statusCode
    )
}

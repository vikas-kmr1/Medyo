package medyo.com.core.data.mapper

import medyo.com.core.ai_logic.dto.AiMedicationResponseDto
import medyo.com.core.database.dao.MedicationDetail
import medyo.com.core.database.entity.MedicationCategory
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicationInfoEntity
import medyo.com.core.domain.model.MedicationInfo

fun AiMedicationResponseDto.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        name = this.brand ?: "Unknown Medication",
        dosageStrength = "", // AI doesn't explicitly return this in current schema
        form = "", 
        category = MedicationCategory.FIRST_AID_STOCK, 
        stockQuantity = 0,
        expiryDate = this.expDate,
        alertDaysBeforeExpiry = null
    )
}

fun AiMedicationResponseDto.toMedicationInfoEntity(medicationId: Long): MedicationInfoEntity {
    return MedicationInfoEntity(
        medicationId = medicationId,
        brand = this.brand,
        salts = this.salts,
        sideEffects = this.sideEffects,
        cures = this.cures,
        precautions = this.precautions,
        instructions = this.instructions,
        mfgDate = this.mfgDate,
    )
}

fun MedicationDetail.toDomainModel(): MedicationInfo {
    return MedicationInfo(
        medicationId = this.medicationId,
        name = this.name,
        brand = this.brand ?: this.name,
        salts = this.salts.orEmpty(),
        mfgDate = this.mfgDate,
        expDate = this.expDate,
        sideEffects = this.sideEffects,
        cures = this.cures,
        precautions = this.precautions,
        instructions = this.instructions,
        category = "", // Not currently joined or mapped directly from DB details, left blank for UI

    )
}

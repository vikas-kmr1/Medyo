package medyo.com.core.data.mapper

import medyo.com.core.ai_logic.dto.AiMedicationResponseDto
import medyo.com.core.database.dao.MedicationDetail
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.database.entity.MedicationInfoEntity
import medyo.com.core.domain.model.MedicationInfo
import medyo.com.core.utils.constants.MedicationCategory
import medyo.com.core.utils.constants.MedicationType
import medyo.com.core.utils.kotlin.emptyString
import medyo.com.core.utils.kotlin.zeroL

fun MedicationInfo.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        name = this.brand,
        dosageStrength = this.totalDoses, // AI doesn't explicitly return this in current schema
        form = this.form,
        category = MedicationCategory.FIRST_AID_STOCK,
        stockQuantity = this.stockQuantity,
        expiryDate = this.expDate,
        alertDaysBeforeExpiry = this.alertDaysBeforeExpiry
    )
}

fun MedicationInfo.toMedicationInfoEntity( medicationId: Long): MedicationInfoEntity {
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

fun AiMedicationResponseDto.toDomainModel(): MedicationInfo {
    return MedicationInfo(
        medicationId = zeroL,
        name = this.brand ?: "Unknown Medication",
        brand = this.brand ?: "Unknown Medication",
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
        form = this.form?: MedicationType.OTHER.name,
        category = MedicationCategory.FIRST_AID_STOCK.name, // Not currently joined or mapped directly from DB details, left blank for UI

    )
}

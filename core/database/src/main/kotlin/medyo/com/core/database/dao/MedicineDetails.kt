package medyo.com.core.database.dao

data class MedicineDetails(
    val medicationId: Long,
    val name: String,
    val dosageStrength: String,
    val brand: String?,
    val salts: String?,
    val sideEffects: List<String>,
    val cures: List<String>,
    val precautions: List<String>,
    val instructions: List<String>,
    val mfgDate: Long?,
    val expDate: Long?,
    val errorMessage: String?,
    val statusCode: String?
)

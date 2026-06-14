package medyo.com.core.utils.constants

enum class MedicationType {
    BANDAGE,
    BLISTER,
    CAPSULE,
    CREAM,
    DROP,
    INHALER,
    INJECTION,
    LOTION,
    AYURVEDIC,
    OINTMENT,
    PATCH,
    TABLET,
    SACHET,
    SOFTGEL,
    SYRUP,
    OTHER
}

enum class MedicationCategory(val label: String, val description: String) {
    RUNNING_DOSE("Schedule Dose", "Linked to Timeline"),   // Needs scheduling
    FIRST_AID_STOCK("First Aid / Stock", "Expiry Track Only") // Expiry tracking only
}
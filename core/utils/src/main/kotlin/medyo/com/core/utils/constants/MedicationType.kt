package medyo.com.core.utils.constants

enum class MedicationType(val label: String) {
    BANDAGE("Bandage"),
    BLISTER("Blister"),
    CAPSULE("Capsule"),
    CREAM("Cream"),
    DROP("Drop"),
    INHALER("Inhaler"),
    INJECTION("Injection"),
    LOTION("Lotion"),
    AYURVEDIC("Ayurvedic"),
    OINTMENT("Ointment"),
    PATCH("Patch"),
    TABLET("Tablet"),
    SACHET("Sachet"),
    SOFTGEL("Softgel"),
    SYRUP("Syrup"),
    OTHER("Other")
}

enum class MedicationCategory(val label: String, val description: String) {
    RUNNING_DOSE("Schedule Dose", "Linked to Timeline"),   // Needs scheduling
    FIRST_AID_STOCK("First Aid / Stock", "Expiry Track Only") // Expiry tracking only
}

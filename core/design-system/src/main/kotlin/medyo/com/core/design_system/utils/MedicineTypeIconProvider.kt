package medyo.com.core.design_system.utils

import medyo.com.core.design_system.R
import medyo.com.core.utils.constants.MedicationType

fun getMedicationIcon(type: MedicationType): Int {
    return when (type) {
        MedicationType.BANDAGE -> R.drawable.ic_bandage
        MedicationType.BLISTER -> R.drawable.ic_blister
        MedicationType.CAPSULE -> R.drawable.ic_capsule
        MedicationType.CREAM -> R.drawable.ic_cream
        MedicationType.DROP -> R.drawable.ic_drop
        MedicationType.INHALER -> R.drawable.ic_inhaler
        MedicationType.INJECTION -> R.drawable.ic_injection
        MedicationType.LOTION -> R.drawable.ic_lotion
        MedicationType.AYURVEDIC -> R.drawable.ic_mortal
        MedicationType.OINTMENT -> R.drawable.ic_oinmemt
        MedicationType.PATCH -> R.drawable.ic_patches
        MedicationType.TABLET -> R.drawable.ic_pill
        MedicationType.SACHET -> R.drawable.ic_sachet
        MedicationType.SOFTGEL -> R.drawable.ic_softgel
        MedicationType.SYRUP -> R.drawable.ic_syrup
        MedicationType.OTHER -> R.drawable.ic_medical_bag
    }
}
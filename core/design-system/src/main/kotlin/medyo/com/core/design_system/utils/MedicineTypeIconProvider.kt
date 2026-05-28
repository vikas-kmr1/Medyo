package medyo.com.core.design_system.utils

import medyo.com.core.design_system.R
import medyo.com.core.utils.constants.MedicineType

fun getMedicineIcon(type: MedicineType): Int {
    return when (type) {
        MedicineType.BANDAGE -> R.drawable.ic_bandage
        MedicineType.BLISTER -> R.drawable.ic_blister
        MedicineType.CAPSULE -> R.drawable.ic_capsule
        MedicineType.CREAM -> R.drawable.ic_cream
        MedicineType.DROP -> R.drawable.ic_drop
        MedicineType.INHALER -> R.drawable.ic_inhaler
        MedicineType.INJECTION -> R.drawable.ic_injection
        MedicineType.LOTION -> R.drawable.ic_lotion
        MedicineType.AYURVEDIC -> R.drawable.ic_mortal
        MedicineType.OINTMENT -> R.drawable.ic_oinmemt
        MedicineType.PATCH -> R.drawable.ic_patches
        MedicineType.TABLET -> R.drawable.ic_pill
        MedicineType.SACHET -> R.drawable.ic_sachet
        MedicineType.SOFTGEL -> R.drawable.ic_softgel
        MedicineType.SYRUP -> R.drawable.ic_syrup
        MedicineType.OTHER -> R.drawable.ic_medical_bag
    }
}
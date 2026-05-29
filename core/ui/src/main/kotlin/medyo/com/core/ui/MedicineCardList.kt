package medyo.com.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import medyo.com.core.design_system.component.card.MedicineCardItem
import medyo.com.core.design_system.component.card.MedicineCardItemData
import medyo.com.core.design_system.theme.LocalDimensions


fun LazyListScope.MedicineCardList(
    medicineItems: List<MedicineCardItemData>
) = items(
    items = medicineItems,
    key = { it.id },
) { medicineCardItem ->
    MedicineCardItem(
        modifier = Modifier.padding(
            vertical = LocalDimensions.current.dimen8dp
        ),
        name = medicineCardItem.name,
        dosage = medicineCardItem.dosage,
        iconRes = medicineCardItem.iconRes,
        iconBackgroundColor = medicineCardItem.iconBackgroundColor,
        cardGradientStartColor = medicineCardItem.cardGradientStartColor,
        shadowColor = medicineCardItem.shadowColor,
        onClick = { }
    )
}



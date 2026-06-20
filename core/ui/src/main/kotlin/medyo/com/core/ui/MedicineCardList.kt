package medyo.com.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import medyo.com.core.design_system.component.card.MedicationCardItem
import medyo.com.core.design_system.component.card.MedicationCardItemData
import medyo.com.core.design_system.theme.LocalDimensions


fun LazyListScope.MedicationCardList(
    MedicationItems: List<MedicationCardItemData>
) = items(
    items = MedicationItems,
    key = { it.id },
) { MedicationCardItem ->
    MedicationCardItem(
        modifier = Modifier.padding(
            vertical = LocalDimensions.current.dimen8dp,
            horizontal = LocalDimensions.current.dimen24dp
        ),
        name = MedicationCardItem.name,
        dosage = MedicationCardItem.dosage,
        iconRes = MedicationCardItem.iconRes,
        iconBackgroundColor = MedicationCardItem.iconBackgroundColor,
        cardGradientStartColor = MedicationCardItem.cardGradientStartColor,
        shadowColor = MedicationCardItem.shadowColor,
        onClick = { }
    )
}


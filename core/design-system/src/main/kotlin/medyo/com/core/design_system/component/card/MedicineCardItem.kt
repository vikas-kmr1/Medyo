package medyo.com.core.design_system.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.R
import medyo.com.core.design_system.component.tooltip.MedyoTooltip
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.LocalIconSize
import medyo.com.core.design_system.theme.LocalTintTheme
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.icon.MedyoIcons
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.utils.getMedicineIcon
import medyo.com.core.utils.constants.MedicineType

@Composable
fun MedicineCardItem(
    modifier: Modifier = Modifier,
    name: String,
    dosage: String,
    iconRes: Int,
    iconBackgroundColor: Color,
    cardGradientStartColor: Color,
    shadowColor: Color, // New parameter for the glowing shadow
    onClick: () -> Unit,
) {


    val shape = LocalAppShapes.current.cardShape
    val dimensions = LocalDimensions.current
    val colorScheme = MaterialTheme.colorScheme


    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensions.dimen20dp,
                shape = shape,
                spotColor = shadowColor.copy(alpha = 0.6f),
                ambientColor = shadowColor.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(colorScheme.background) // Base color
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        // colorStops force the gradient to fully fade to transparent white by 45% of the width
                        0.0f to cardGradientStartColor.copy(alpha = 0.5f),
                        0.45f to colorScheme.background.copy(alpha = 0f),
                        1.0f to colorScheme.background.copy(alpha = 0f)
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.dimen16dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MedicineIcon(
                iconBackgroundColor = iconBackgroundColor,
                iconRes = iconRes,
                name = name,
            )

            Spacer(modifier = Modifier.width(dimensions.dimen16dp))

            MedicineInfo(
                modifier = Modifier.weight(1f),
                name = name,
                dosage = dosage,
            )
            // Right Arrow Indicator
            MedyoTooltip(
                text = MedyoIcons.seemore.contentDescription?:"See More",
            ) {
                Icon(
                    imageVector = MedyoIcons.seemore.icon,
                    contentDescription = MedyoIcons.seemore.contentDescription,
                    tint = LocalTintTheme.current.iconTint,
                    modifier = Modifier.size(LocalIconSize.current.default)
                )
            }
        }
    }
}

@Composable
private fun MedicineIcon(
    iconBackgroundColor: Color,
    iconRes: Int,
    name: String,
) {
    Box(
        modifier = Modifier
            .size(52.dp) // Slightly larger to match the premium feel
            .clip(RoundedCornerShape(16.dp)) // More rounded
            .background(iconBackgroundColor.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "$name Icon",
            modifier = Modifier.matchParentSize().padding(2.dp),
            tint = Color.Unspecified
        )
    }

}

@Composable
private fun MedicineInfo(
    modifier: Modifier = Modifier,
    name: String,
    dosage: String,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(LocalDimensions.current.dimen2dp)) // Small gap for readability
        Text(
            text = dosage,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }


}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun MedicineCardItemPreview() {
    MedyoTheme {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Generous spacing between cards
        ) {
          sampleMedicineList.forEach { medicine ->
              MedicineCardItem(

                  name = medicine.name,
                  dosage = medicine.dosage,
                  iconRes = medicine.iconRes,
                  iconBackgroundColor = medicine.iconBackgroundColor,
                  cardGradientStartColor = medicine.cardGradientStartColor,
                  shadowColor = medicine.shadowColor,
                  onClick = {},
              )
          }
        }
    }
}

data class MedicineCardItemData(
    val id: Int, // Important for LazyColumn keys
    val name: String,
    val dosage: String,
    val iconRes: Int,
    val iconBackgroundColor: Color,
    val cardGradientStartColor: Color,
    val shadowColor: Color
)

val sampleMedicineList = listOf(
    MedicineCardItemData(
        id = 1,
        name = "Amoxicillin 500mg",
        dosage = "500mg",
        iconRes = android.R.drawable.ic_menu_camera,
        iconBackgroundColor = Color(0xFF00ACC1),
        cardGradientStartColor = Color(0xFF4DD0E1),
        shadowColor = Color(0xFF00ACC1)
    ),
    MedicineCardItemData(
        id = 2,
        name = "Lipitor 10mg",
        dosage = "10mg",
        iconRes = android.R.drawable.ic_menu_camera,
        iconBackgroundColor = Color(0xFFFF9800),
        cardGradientStartColor = Color(0xFFFFB74D),
        shadowColor = Color(0xFFFF9800)
    ),
    MedicineCardItemData(
        id = 3,
        name = "Lipitor 10mg", // Note: Same name but different ID and Icon
        dosage = "10mg",
        iconRes = getMedicineIcon(MedicineType.INJECTION),
        iconBackgroundColor = Color.White, // Using hardcoded or MaterialTheme colors
        cardGradientStartColor = Color.White,
        shadowColor = Color.Black
    ),
    MedicineCardItemData(
        id = 4,
        name = "Amoxicillin 500mg",
        dosage = "500mg",
        iconRes = android.R.drawable.ic_menu_camera,
        iconBackgroundColor = Color(0xFF00ACC1),
        cardGradientStartColor = Color(0xFF4DD0E1),
        shadowColor = Color(0xFF00ACC1)
    ),
    MedicineCardItemData(
        id = 5,
        name = "Lipitor 10mg",
        dosage = "10mg",
        iconRes = android.R.drawable.ic_menu_camera,
        iconBackgroundColor = Color(0xFFFF9800),
        cardGradientStartColor = Color(0xFFFFB74D),
        shadowColor = Color(0xFFFF9800)
    ),
    MedicineCardItemData(
        id = 6,
        name = "Lipitor 10mg", // Note: Same name but different ID and Icon
        dosage = "10mg",
        iconRes = getMedicineIcon(MedicineType.INJECTION),
        iconBackgroundColor = Color.White, // Using hardcoded or MaterialTheme colors
        cardGradientStartColor = Color.White,
        shadowColor = Color.Black
    )


)

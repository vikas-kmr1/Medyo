package medyo.com.core.design_system.component.tooltip

import android.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.PopupPositionProvider
import medyo.com.core.design_system.component.card.MedicineCardItem
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedyoTooltip(
    tooltipText: String,
    modifier: Modifier = Modifier,
    isRichTooltip: Boolean = false,
    position: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    content: @Composable () -> Unit,
) {

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            position,
            spacingBetweenTooltipAndAnchor = LocalDimensions.current.dimen8dp,
        ),
        tooltip = {
            if (isRichTooltip) {
                RichTooltip {
                    Text(text = tooltipText)
                }
            } else {
                PlainTooltip(
                    caretShape = TooltipDefaults.caretShape(
                        DpSize(LocalDimensions.current.dimen8dp, LocalDimensions.current.dimen10dp)
                    )
                ) {
                    Text(text = tooltipText)
                }
            }
        },
        state = rememberTooltipState(),
        modifier = modifier,
        content = content,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedyoTooltip(
    text: String,
    modifier: Modifier = Modifier,
    rich: Boolean = false,
    positionProvider: PopupPositionProvider = TooltipDefaults.rememberTooltipPositionProvider(
        TooltipAnchorPosition.Above,
        spacingBetweenTooltipAndAnchor = LocalDimensions.current.dimen8dp
    ),
    tooltip: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current
    val colorScheme = MaterialTheme.colorScheme
    TooltipBox(
        positionProvider = positionProvider,
        tooltip = {
            tooltip?.invoke() ?: if (rich) {
                RichTooltip(
                    caretShape = TooltipDefaults.caretShape(
                        DpSize(dimensions.dimen8dp, dimensions.dimen10dp)
                    ),
                    colors = TooltipDefaults.richTooltipColors(
                        contentColor = colorScheme.surfaceVariant,
                        containerColor = colorScheme.onSurfaceVariant
                    )
                )
                {
                    Text(text = text)
                }
            } else {
                PlainTooltip(
                    caretShape = TooltipDefaults.caretShape(
                        DpSize(dimensions.dimen8dp, dimensions.dimen10dp)
                    ),
                    contentColor = colorScheme.surfaceVariant,
                    containerColor = colorScheme.onSurfaceVariant
                ) {
                    Text(text = text)
                }
            }
        },
        state = rememberTooltipState(),
        modifier = modifier,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewTooltip() {
    MedyoTheme {
        MedyoTooltip(
            text = "Medicine info",
            modifier = Modifier,
            rich = true,
        ) {
            MedicineCardItem(
                name = "Lipitor 10mg",
                dosage = "10mg",
                iconRes = R.drawable.ic_menu_camera,
                iconBackgroundColor = Color(0xFFFF9800),
                cardGradientStartColor = Color(0xFFFFB74D),
                shadowColor = Color(0xFFFF9800),
                onClick = {},
            )
        }
    }
}
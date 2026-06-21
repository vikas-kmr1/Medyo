package medyo.com.core.design_system.component.bullet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.theme.HighestContrast
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.utils.compose.CommonPreview


@Composable
fun CircularOutlineBullet(
    dimension: Dp = LocalDimensions.current.dimen8dp
) {
    Box(
        modifier = Modifier
            .size(dimension)
            .clip(LocalAppShapes.current.chipShape)
            .border(
                1.5.dp, HighestContrast,
                shape = LocalAppShapes.current.chipShape
            )
    )
}

@Composable
fun CircularBullet(
    dimension: Dp = LocalDimensions.current.dimen8dp
) {
    Box(
        modifier = Modifier
            .size(dimension)
            .clip(LocalAppShapes.current.chipShape)
            .background(HighestContrast)
    )
}

@CommonPreview
@Composable
private fun PreviewCircularBullet() {
    MedyoTheme {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CircularBullet(8.dp)
            CircularOutlineBullet(8.dp)
        }
    }
}

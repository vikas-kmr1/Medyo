package medyo.com.core.design_system.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Immutable
data class IconSizes(
    val extraSmall: Dp = 8.dp,
    val small: Dp  = 12.dp,
    val semiMedium: Dp  = 16.dp,
    val default: Dp  = 24.dp,
    val medium: Dp  = 28.dp,
    val semiLarge: Dp  = 36.dp,
    val large: Dp  = 50.dp,
    val extraLarge: Dp  = 200.dp,
    val largerBanner: Dp  = 200.dp
)

@Immutable
data class Spacing(
    val microSmall: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val semi_medium: Dp = 10.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
    val xLarge: Dp = 64.dp,
    val xxLarge: Dp = 96.dp,
    val xxxLarge: Dp = 128.dp
)

val LocalSpacing = staticCompositionLocalOf{ Spacing() }

val  LocalIconSize = staticCompositionLocalOf{IconSizes() }

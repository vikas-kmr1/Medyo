package medyo.com.core.design_system.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.yuma.franchise.ui.theme.shapes.LocalAppShapes
import com.yuma.franchise.ui.theme.shapes.MedyoShapes
import com.yuma.franchise.ui.theme.shapes.appShapes

//private val DarkColorScheme = darkColorScheme(
//    primary = NeonCyan,
//    secondary = NeonOrange,
//    background = Color.Black,
//    surface = NavyMedium,
//    onBackground = Color.White,
//    onSurface = Color.White.copy(alpha = 0.8f)
//)

private val LightColorScheme = lightColorScheme(
    primary = TrackCyanLight,
    secondary = TrackCyanVariant,
    tertiary = TrackAmberLight,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onPrimary = LightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextMuted,
    outline = LightBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = TrackCyanDark,
    secondary = TrackCyanDark,
    tertiary = TrackAmberDark,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkBackground,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextMuted,
    outline = DarkBorder
)

@Composable
fun MedyoTheme(
    darkTheme: Boolean = false,//isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDimensions provides Dimensions(),
        LocalSpacing provides Spacing(),
        LocalIconSize provides IconSizes(),
        LocalTintTheme provides TintTheme(colorScheme.onSurfaceVariant),
        LocalAppShapes provides appShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
            shapes = MedyoShapes
        )
    }
}
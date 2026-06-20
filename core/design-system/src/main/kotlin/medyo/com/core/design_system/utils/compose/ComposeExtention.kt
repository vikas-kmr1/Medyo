package medyo.com.core.design_system.utils.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import medyo.com.core.design_system.component.shimmer.shimmerBrush
import java.lang.System.currentTimeMillis


@Composable
fun Modifier.shimmerBackground(
    showShimmer: Boolean = true,
    targetValue: Float = 7000f
): Modifier = this.background(
    shimmerBrush(
        showShimmer = showShimmer,
        targetValue = targetValue
    )
)


fun Modifier.gesturesEnabled(enabled: Boolean = true) =
    if (!enabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                // we should wait for all new pointer events
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }

inline fun Modifier.debounceClickable(
    enabled: Boolean = true,
    debounceInterval: Long = 1000L,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by rememberSaveable { mutableLongStateOf(0L) }
    clickable(enabled = enabled) {
        val currentTime = currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}

inline fun Modifier.noRippleDebounceClickable(
    enabled: Boolean = true,
    debounceInterval: Long = 1000L,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by rememberSaveable { mutableLongStateOf(0L) }
    clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        val currentTime = currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}

fun Modifier.animatedGradient(
    primaryColor: Color,
    containerColor: Color
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "")
    
    val animationProgress by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "gradientAnimation"
    )

    this.drawBehind {
        drawRect(color = containerColor)
        
        val cornerRadius = (size.minDimension * animationProgress).coerceAtLeast(1f)
        val transparentPrimary = primaryColor.copy(alpha = 0f)
        val cornerColors = listOf(primaryColor, transparentPrimary)

        // Top Left
        drawRect(
            brush = Brush.radialGradient(
                colors = cornerColors,
                center = Offset(0f, 0f),
                radius = cornerRadius
            )
        )
        // Top Right
        drawRect(
            brush = Brush.radialGradient(
                colors = cornerColors,
                center = Offset(size.width, 0f),
                radius = cornerRadius
            )
        )
        // Bottom Left
        drawRect(
            brush = Brush.radialGradient(
                colors = cornerColors,
                center = Offset(0f, size.height),
                radius = cornerRadius
            )
        )
        // Bottom Right
        drawRect(
            brush = Brush.radialGradient(
                colors = cornerColors,
                center = Offset(size.width, size.height),
                radius = cornerRadius
            )
        )
    }
}

fun Dp.toDpSize() = DpSize(this, this)

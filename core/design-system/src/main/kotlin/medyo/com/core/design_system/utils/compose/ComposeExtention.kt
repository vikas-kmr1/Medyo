package medyo.com.core.design_system.utils.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
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

fun Dp.toDpSize() = DpSize(this, this)

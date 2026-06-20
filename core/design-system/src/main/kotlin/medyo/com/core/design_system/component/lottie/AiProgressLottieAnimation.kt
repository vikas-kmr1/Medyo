package medyo.com.core.design_system.component.lottie

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.R
import medyo.com.core.design_system.component.GeminiBananaFluidBackground
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.design_system.utils.compose.animatedGradient

@Composable
fun AiProgressLottieAnimation() {
    Box(modifier = Modifier.fillMaxSize().animatedGradient(MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f), MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center) {
        ShowLottieAnimation(
            animationJsonResId = R.raw.ai_progress,
            repeat = true,
        )
    }
}

@CommonPreview
@Composable
private fun AiProgressLottieAnimationPreview() {
    MedyoTheme() {
        Box(modifier = Modifier.fillMaxSize()) {
            AiProgressLottieAnimation()
        }
    }
}
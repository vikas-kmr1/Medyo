package medyo.com.core.design_system.component.lottie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import medyo.com.core.design_system.R
import medyo.com.core.design_system.theme.LocalIconSize
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview


@Composable
internal fun ShowLottieAnimation(
    modifier: Modifier = Modifier,
    animationJsonResId: Int,
    onComplete: () -> Unit = {},
    speed: Float = 1f,
    repeat: Boolean = false,
    scale: ContentScale = ContentScale.Fit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationJsonResId))
    val logoAnimationState = animateLottieCompositionAsState(
        composition = composition,
        speed = speed,
        iterations = if (repeat) LottieConstants.IterateForever else 1
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { logoAnimationState.progress },
        contentScale = scale
    )
    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
        onComplete()
    }
}


@CommonPreview
@Composable
private fun ShowLottieAnimationPreview() {
    MedyoTheme {
        Box {
            ShowLottieAnimation(
                modifier = Modifier
                    .size(LocalIconSize.current.largerBanner)
                    .zIndex(100f)
                    .background(Color.Transparent),
                animationJsonResId = R.raw.ai_progress,
                repeat = true
            )
        }
    }
}
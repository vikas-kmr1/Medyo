package medyo.com.core.design_system.component.lottie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import medyo.com.core.design_system.R
import medyo.com.core.design_system.utils.compose.CommonPreview

@Composable
fun AiProgressLottieAnimation() {
    Box(modifier = Modifier.fillMaxSize(),
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
    Box(modifier = Modifier.fillMaxSize()) {
        AiProgressLottieAnimation()
    }
}
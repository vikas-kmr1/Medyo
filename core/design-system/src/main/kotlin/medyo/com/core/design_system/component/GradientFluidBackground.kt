package medyo.com.core.design_system.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import medyo.com.core.design_system.utils.compose.CommonPreview
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun GeminiBananaFluidBackground(modifier: Modifier = Modifier) {
    // Exact colors from the video reference
    val aiCyan = Color(0xFF22D3EE)
    val bananaYellow = Color(0xFFD97706)
    val cyberPurple = Color(0xFF6366F1)
    val warmAmber = Color(0xFFFB923C)

    val infiniteTransition = rememberInfiniteTransition(label = "FluidMotionLight")

    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "FluidTimeLight"
    )

    // The Canvas background is natively transparent unless filled.
    // We leave it empty so it floats over your white UI.
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val baseRadius = minOf(w, h) * 0.7f

        // 1. AI Cyan Movement
        val cyanX = w * 0.4f + (cos(time) * 150f).toFloat()
        val cyanY = h * 0.3f + (sin(time) * 100f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(aiCyan.copy(alpha = 0.5f), Color.Transparent),
                center = Offset(cyanX, cyanY),
                radius = baseRadius
            ),
            radius = baseRadius,
            center = Offset(cyanX, cyanY),
            blendMode = BlendMode.Multiply // Creates a rich watercolor overlap on light surfaces
        )

        // 2. Banana Yellow Movement
        val yellowX = w * 0.6f + (sin(time + 2f) * 200f).toFloat()
        val yellowY = h * 0.4f + (cos(time + 1f) * 150f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(bananaYellow.copy(alpha = 0.45f), Color.Transparent),
                center = Offset(yellowX, yellowY),
                radius = baseRadius
            ),
            radius = baseRadius,
            center = Offset(yellowX, yellowY),
            blendMode = BlendMode.Multiply
        )

        // 3. Cyber Purple Ambient
        val purpleX = w * 0.3f + (cos(time * 0.5f) * 120f).toFloat()
        val purpleY = h * 0.6f + (sin(time * 0.8f) * 180f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(cyberPurple.copy(alpha = 0.4f), Color.Transparent),
                center = Offset(purpleX, purpleY),
                radius = baseRadius * 1.2f
            ),
            radius = baseRadius * 1.2f,
            center = Offset(purpleX, purpleY),
            blendMode = BlendMode.Multiply
        )

        // 4. Amber Core
        val amberX = w * 0.5f + (cos(time * 1.2f) * 100f).toFloat()
        val amberY = h * 0.5f + (sin(time * 0.9f) * 100f).toFloat()

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(warmAmber.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(amberX, amberY),
                radius = baseRadius * 0.8f
            ),
            radius = baseRadius * 0.8f,
            center = Offset(amberX, amberY),
            blendMode = BlendMode.Multiply
        )
    }
}

@CommonPreview
@Composable
private fun LoadingPlaceholderCardPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1: The background aura engine
        GeminiBananaFluidBackground(
            modifier = Modifier.align(Alignment.TopStart)
        )

    }
}
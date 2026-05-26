package medyo.com.core.design_system.component.navigation

import android.graphics.Shader
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import medyo.com.core.design_system.theme.MedyoTheme

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    initialTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    // 1. STATE MANAGEMENT: Track which tab is active (0 = Home, 1 = Settings)
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    // 2. COLORS: Matching the glowing wave design
    val inactiveColor = Color(0xFFCBD5E1) // Muted Slate Gray
    val activeHomeColor = Color(0xFF00ACC1) // Cyan (Left side of wave)
    val activeSettingsColor = Color(0xFFFF9800) // Orange (Right side of wave)

    // 3. ANIMATION: Smoothly fade colors when clicked
    val homeIconTint by animateColorAsState(
        targetValue = if (selectedTab == 0) activeHomeColor else inactiveColor,
        animationSpec = tween(durationMillis = 300),
        label = "Home Color Animation"
    )

    val settingsIconTint by animateColorAsState(
        targetValue = if (selectedTab == 1) activeSettingsColor else inactiveColor,
        animationSpec = tween(durationMillis = 300),
        label = "Settings Color Animation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
    ) {
        // Glowing Wavy Divider (Unchanged)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val wavePath = Path().apply {
                moveTo(0f, height * 0.35f)
                lineTo(width * 0.30f, height * 0.35f)
                cubicTo(
                    x1 = width * 0.38f, y1 = height * 0.35f,
                    x2 = width * 0.40f, y2 = height * 0.85f,
                    x3 = width * 0.50f, y3 = height * 0.85f
                )
                cubicTo(
                    x1 = width * 0.60f, y1 = height * 0.85f,
                    x2 = width * 0.62f, y2 = height * 0.35f,
                    x3 = width * 0.70f, y3 = height * 0.35f
                )
                lineTo(width, height * 0.35f)
            }

            val waveGradientColors = intArrayOf(
                "#00ACC1".toColorInt(),
                "#FF9800".toColorInt()
            )

            drawContext.canvas.nativeCanvas.apply {
                val blurPaint = android.graphics.Paint().apply {
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = 8.dp.toPx()
                    alpha = 130
                    shader = android.graphics.LinearGradient(
                        0f, 0f, width, 0f,
                        waveGradientColors,
                        null,
                        Shader.TileMode.CLAMP
                    )
                    maskFilter = android.graphics.BlurMaskFilter(20f, android.graphics.BlurMaskFilter.Blur.NORMAL)
                }
                drawPath(wavePath.asAndroidPath(), blurPaint)
            }

            drawPath(
                path = wavePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF00ACC1), Color(0xFFFF9800))
                ),
                style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Icons Row
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Home Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        // Removes the ugly default rectangular ripple for a cleaner look
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedTab = 0
                        onTabSelected(0)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home",
                    tint = homeIconTint, // Apply animated color here
                    modifier = Modifier.size(28.dp)
                )
            }

            // Settings Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedTab = 1
                        onTabSelected(1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = settingsIconTint, // Apply animated color here
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Central Logo Button (Unchanged)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = Color(0xFF00ACC1).copy(alpha = 09f),
                    ambientColor = Color(0xFF00ACC1).copy(alpha = 0.2f)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00ACC1),Color(0xFF00ACC1))
                    ),
                    shape = CircleShape
                )
                .clickable { /* Central action */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "M",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00ACC1),
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFFF1F5F9)
@Composable
fun CustomBottomNavigationPreview() {
    val selectedTab = remember { mutableIntStateOf(0) }
    MedyoTheme() {
        Box(
            modifier = Modifier.fillMaxSize().safeContentPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomBottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                initialTab = selectedTab.intValue,
                onTabSelected = { selectedTab.intValue = it }
            )
        }
    }
}
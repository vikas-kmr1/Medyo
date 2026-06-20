package medyo.com.core.design_system.component.button

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BioScanWidget(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Base surface creates the circular shape and the glowing cyan shadow
    Surface(
        modifier = modifier
            .size(260.dp) // Large prominent size for the middle of the screen
            .shadow(
                elevation = 24.dp,
                shape = CircleShape,
                spotColor = Color(0xFF00ACC1).copy(alpha = 0.5f), // Cyan glow shadow
                ambientColor = Color(0xFF00ACC1).copy(alpha = 0.2f)
            )
            .clip(CircleShape)
            .clickable { onClick() },
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Inner Radial Gradient for that soft glowing effect around the edges
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFE0F7FA).copy(alpha = 0.7f) // Light cyan at the rim
                            ),
                            radius = 350f
                        )
                    )
            )

            // 2. High-Tech Circuit Lines (Drawn natively so it scales perfectly)
            CircuitBackground()

            // 3. Central Content (Texts & Camera Icon)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Top Header Text
                Text(
                    text = "INTEGRATED\nBIO-SCAN",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF00ACC1), // Vibrant cyan text
                        lineHeight = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // The central camera icon inside a soft circle
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = "Camera Icon",
                        tint = Color(0xFF00ACC1), // Match cyan theme
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Call-to-action text
                Text(
                    text = "TAP TO MAP YOUR\nPRESCRIPTION",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFF64748B), // Slate gray text for subtle contrast
                        lineHeight = 14.sp
                    )
                )
            }
        }
    }
}

/**
 * Draws the tech/circuit lines inside the circle behind the camera icon.
 */
@Composable
private fun CircuitBackground() {
    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val strokeWidth = 3f
        val color = Color(0xFF4DD0E1).copy(alpha = 0.4f) // Soft cyan for the lines

        // A helper function to draw circuit-like "bent" lines ending with a dot
        fun drawCircuitPath(startX: Float, startY: Float, midX: Float, midY: Float, endX: Float, endY: Float) {
            val path = Path().apply {
                moveTo(startX, startY)
                lineTo(midX, midY)
                lineTo(endX, endY)
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
            drawCircle(color = color, radius = 6f, center = Offset(startX, startY)) // Outer dot
        }

        // Left Top Circuit
        drawCircuitPath(
            startX = 20f, startY = center.y - 40f,
            midX = center.x - 60f, midY = center.y - 40f,
            endX = center.x - 40f, endY = center.y - 15f
        )

        // Left Bottom Circuit
        drawCircuitPath(
            startX = 40f, startY = center.y + 60f,
            midX = center.x - 50f, midY = center.y + 60f,
            endX = center.x - 30f, endY = center.y + 35f
        )

        // Right Top Circuit
        drawCircuitPath(
            startX = size.width - 20f, startY = center.y - 60f,
            midX = center.x + 50f, midY = center.y - 60f,
            endX = center.x + 30f, endY = center.y - 35f
        )

        // Right Bottom Circuit
        drawCircuitPath(
            startX = size.width - 40f, startY = center.y + 40f,
            midX = center.x + 60f, midY = center.y + 40f,
            endX = center.x + 40f, endY = center.y + 15f
        )
    }
}

// --- PREVIEW ---

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
fun BioScanWidgetPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        BioScanWidget(onClick = { /* Launch Camera Scanner */ })
    }
}
package medyo.com.core.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.theme.GlassWhite
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.NavyMedium


@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        GlassWhite.copy(alpha = 0.2f),
                        Color.Transparent,
                    ),
                )
            ) // Translucent base
            .border(
                width = 1.5.dp,
                color = GlassWhite.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .blur(0.dp) // Frosted glass effect
    ) {
        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            content()
        }
    }
}

@Composable
fun PrimaryGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        NavyMedium.copy(alpha = 0.6f),
                        GlassWhite.copy(alpha = 0.1f),
                        Color.Transparent,
                        NavyMedium.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                )
            ) // Translucent base
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, Color.Transparent, Color.White)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .blur(0.dp) // Frosted glass effect
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Preview(showBackground = true, device = PIXEL_9_PRO_XL)
@Composable
private fun GlassCardPreview() {
    MedyoTheme () {
        Column() {
            CardItem()
            PrimaryCardItem()
        }
    }
}

@Composable
fun CardItem() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(10.dp)
    ) {
        GlassCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = "Amoxicillin 500mg", color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "morning",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "see more",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryCardItem() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(10.dp)
    ) {
        PrimaryGlassCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = "Amoxicillin 500mg", color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "morning",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "see more",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
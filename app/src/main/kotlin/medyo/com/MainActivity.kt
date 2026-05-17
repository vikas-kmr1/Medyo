package medyo.com

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import medyo.com.ui.theme.GlassWhite
import medyo.com.ui.theme.MedyoTheme
import medyo.com.ui.theme.NavyMedium

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreenInstance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            null
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // loading mock data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && splashScreenInstance != null) {
            var keepSplashScreen = true
            // Keep the splash screen on-screen for 3 seconds
            splashScreenInstance.setKeepOnScreenCondition { keepSplashScreen }
            lifecycleScope.launch {
                delay(1000L)
                keepSplashScreen = false
            }

            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val fadeOut = ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f)
                fadeOut.duration = 500L
                fadeOut.doOnEnd { splashScreenView.remove() }
                fadeOut.start()
            }

        }

        setContent {
            MedyoTheme {
                Surface {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Medyo",
                                        style = MaterialTheme.typography.displayLarge
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                )
                            )
                        }
                    ) { innerPadding ->
                        LazyColumn(modifier = Modifier.padding(innerPadding)) {
                            items(1) {
                                PrimaryCardItem()
                            }
                            items(10) {
                                CardItem()
                            }
                        }

                    }
                }
            }
        }
    }
}

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
        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            content()
        }
    }
}

@Preview(showBackground = true, device = PIXEL_9_PRO_XL)
@Composable
private fun GlassCardPreview() {
    MedyoTheme() {
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


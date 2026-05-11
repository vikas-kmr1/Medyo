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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
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
                delay(3000L)
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
                            .background(Color.Black)
                    ) { innerPadding ->
                        GlassCard(modifier = Modifier.padding(innerPadding)) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(15.dp), text = "Hello World", color = Color.White,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    style = MaterialTheme.typography.displayLarge
                                )
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
            .clip(RoundedCornerShape(60.dp))
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
                shape = RoundedCornerShape(60.dp)
            )
            .blur(0.dp) // Frosted glass effect
    ) {
        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            content()
        }
    }
}

@Preview(showBackground = true, device = PIXEL_7_PRO)
@Composable
private fun GlassCardPreview() {
    MedyoTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .size(200.dp)
                .padding(10.dp)
        ) {
            GlassCard {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(15.dp), text = "Hello World", color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
package medyo.com

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import medyo.com.core.design_system.component.CardItem
import medyo.com.core.design_system.component.PrimaryCardItem
import medyo.com.core.design_system.theme.MedyoTheme


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreenInstance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            null
        }
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
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



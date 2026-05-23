package medyo.com

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import medyo.com.ai_logic.GeminiAiDataSource
import medyo.com.core.design_system.component.card.MedicineCardItem
import medyo.com.core.design_system.theme.MedyoTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreenInstance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            null
        }

        val genAi = GeminiAiDataSource()
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
                var text  by remember { mutableStateOf("Hello, World!") }
                val scope = rememberCoroutineScope()
                Surface {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        topBar = {}
                    ) { innerPadding ->
                        LazyColumn(modifier = Modifier.padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            item{
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp) // Generous spacing between cards
                                ) {
                                    // Amoxicillin Card
                                    MedicineCardItem(
                                        name = "azithromycin 250",
                                        dosage = "500mg",
                                        iconRes = android.R.drawable.ic_menu_camera,
                                        iconBackgroundColor = Color(0xFF00ACC1),
                                        cardGradientStartColor = Color(0xFF4DD0E1),
                                        shadowColor = Color(0xFF00ACC1), // Cyan shadow glow
                                        onClick = {
                                            scope.launch {
                                                text = genAi.generateContext("azithromycin 250")
                                            }
                                        }
                                    )

                                    // Lipitor Card
                                    MedicineCardItem(
                                        name = "Lipitor 10mg",
                                        dosage = "10mg",
                                        iconRes = android.R.drawable.ic_menu_camera,
                                        iconBackgroundColor = Color(0xFFFF9800),
                                        cardGradientStartColor = Color(0xFFFFB74D),
                                        shadowColor = Color(0xFFFF9800), // Orange shadow glow
                                        onClick = {
                                        }
                                    )


                                    Text(text = text)

                                }
                            }

                        }

                    }
                }
            }
        }
    }
}



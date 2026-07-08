package medyo.com

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import medyo.com.core.ai_logic.GeminiAiDataSource
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.notification.api.Notifier
import medyo.com.ui.MedyoApp
import javax.inject.Inject


@AndroidEntryPoint
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
                MedyoApp()
            }
        }
    }
}



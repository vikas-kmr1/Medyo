package medyo.com.core.design_system.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import medyo.com.core.design_system.R
import medyo.com.core.design_system.utils.compose.CommonPreview


// Display Font: Plus Jakarta Sans
val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
)

// Body Font: Inter
val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_light, FontWeight.Light)
)

// Technical Font: JetBrains Mono
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_medium, FontWeight.Medium)
)


// Set of Material typography styles to start with
val Typography  = Typography(
    // Large Titles (Plus Jakarta Sans)
    displayLarge = TextStyle(
        fontFamily = PlusJakartaSans, // Custom font family
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp,
    ),
   headlineMedium = TextStyle(
        fontFamily = PlusJakartaSans, // Custom font family,
        color = HighestContrast,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp,
       fontSize = 28.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = PlusJakartaSans, // Custom font family
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = (-0.5).sp,
    ),

    titleMedium = TextStyle(
        fontFamily = PlusJakartaSans, // Custom font family
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = (-0.5).sp,
    ),


    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        ),

    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        ),
    // Standard Labels (Inter)
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 24.sp,

    ),
    // Technical/AI Data (JetBrains Mono)
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    )
)

@CommonPreview
@Composable
private fun YumaTypographyPreview(){
    MedyoTheme() {
        Surface(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            color = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(text = "Yuma Typography Preview", style = Typography.displayLarge)
                Text(text = "Yuma Typography Preview", style = Typography.headlineMedium)
                Text(text = "Yuma Typography Preview", style = Typography.titleLarge)
                Text(text = "Yuma Typography Preview", style = Typography.titleMedium)
                Text(text = "Yuma Typography Preview", style = Typography.bodyLarge)
                Text(text = "Yuma Typography Preview", style = Typography.bodySmall)
                Text(text = "Yuma Typography Preview", style = Typography.labelSmall)
            }
        }
    }
}
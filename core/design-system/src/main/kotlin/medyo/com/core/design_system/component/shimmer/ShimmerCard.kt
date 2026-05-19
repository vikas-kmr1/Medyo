package medyo.com.core.design_system.component.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.LightGray
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    shimmerBrush(
                        targetValue = 4000f
                    )
                ),
            verticalArrangement = Arrangement.SpaceAround
        ) {
        }
    }
}



@CommonPreview
@Composable
private fun PreviewTaskCard() {
    MedyoTheme {
        Surface(modifier = Modifier.padding(1.dp)) {
            ShimmerCard()
        }
    }
}







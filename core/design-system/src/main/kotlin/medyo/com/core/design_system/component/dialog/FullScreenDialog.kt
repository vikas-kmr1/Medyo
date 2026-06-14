package medyo.com.core.design_system.component.dialog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview

@Composable
fun FullScreenDialog(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = LocalDimensions.current.dimen16dp,
                    start = LocalDimensions.current.dimen24dp,
                    end = LocalDimensions.current.dimen24dp
                )
            ),
    ) {
        content()
    }
}


@CommonPreview
@Composable
private fun MedicationEditScreenPreview() {
    MedyoTheme {
        FullScreenDialog {
            Text("full screen dialog")
        }
    }
}
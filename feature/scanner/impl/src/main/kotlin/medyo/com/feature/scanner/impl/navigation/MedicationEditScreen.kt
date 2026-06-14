package medyo.com.feature.scanner.impl.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import medyo.com.core.design_system.component.dialog.FullScreenDialog
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview

@Composable
fun MedicationEditScreen() {
    FullScreenDialog(){
          Text("full screen dialog")
    }

}


@CommonPreview
@Composable
private fun MedicationEditScreenPreview() {
    MedyoTheme {
        MedicationEditScreen()
    }
}
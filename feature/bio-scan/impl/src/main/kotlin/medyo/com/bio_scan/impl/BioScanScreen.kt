package medyo.com.bio_scan.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import medyo.com.core.design_system.component.button.BioScanWidget
import medyo.com.core.design_system.component.card.sampleMedicineList
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.ui.MedicineCardList

@Composable
internal fun BioScanScreen(
    onBioScanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        stickyHeader {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BioScanWidget(
                    onClick = onBioScanClick,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Today's Medications", style = MaterialTheme.typography.titleLarge
                )
            }
        }
        MedicineCardList(
            medicineItems = sampleMedicineList,
        )
    }

}

@Composable
@CommonPreview
private fun BioScanScreenPreview() {
    MedyoTheme {
        BioScanScreen(onBioScanClick = {})
    }
}
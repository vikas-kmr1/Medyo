package medyo.com.bio_scan.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import medyo.com.core.design_system.component.button.BioScanWidget
import medyo.com.core.design_system.component.card.sampleMedicineList
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.ui.MedicineCardList


@Preview(showBackground = true)
@Composable
internal fun BioScanScreen() {
    MedyoTheme() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        ) {
            BioScanWidget(
                onClick = {},
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Today's Medications", style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier.fillMaxHeight(.5f),
            ) {
                MedicineCardList(
                    medicineItems = sampleMedicineList,
                )
            }
        }
    }
}
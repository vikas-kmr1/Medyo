package medyo.com.feature.detail.impl

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.component.bullet.CircularBullet
import medyo.com.core.design_system.component.bullet.CircularOutlineBullet
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.design_system.utils.getMedicationIcon
import medyo.com.core.utils.constants.MedicationType


@Composable
fun MedicationDetailScreen() {
    Scaffold() { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MedicationDetailContent()
        }
    }

}

@Composable
private fun MedicationDetailContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(LocalDimensions.current.defaultContentPadding),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.dimen16dp)
    ) {
        item {
            MedicationHeader()
        }
        item {

            MedicationValidaty(
                manufacturedDate = "01 Jan 2024",
                expiryDate = "01 Jan 2027"
            )
        }

        item {
            Text(
                "Primary Uses",
                style = MaterialTheme.typography.titleLarge,
            )
            MedicationCureInfo(cures = listOf("Dizzinesss", "Body Pain", "Headache 3"))
        }
        item {
            Text(
                "Side Effects",
                style = MaterialTheme.typography.titleLarge,
            )
            MedicationSideEffects(sideEffects = listOf("Dizzinesss", "Body Pain", "Headache 3"))
        }
        item {
            Text(
                "Precautions",
                style = MaterialTheme.typography.titleLarge,
            )
            MedicationPrecautions(precautions = listOf("Dizzinesss", "Body Pain", "Headache 3"))
        }
        item {
            Text(
                "Instructions",
                style = MaterialTheme.typography.titleLarge,
            )
            MedicationInstructions(
                instructions = listOf(
                    "Take 1 tablet every 2 hours",
                    "Take 1 tablet every 2 hours"
                )
            )
        }

    }

}

@Composable
private fun MedicationHeader() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MedicationIcon(
            iconRes = getMedicationIcon(MedicationType.CAPSULE),
            name = MedicationType.CAPSULE.name.lowercase(),
            onIconClick = {}
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Health Explorer",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Medicine Name",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(text = "100mg", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun MedicationIcon(
    modifier: Modifier = Modifier,
    iconRes: Int,
    name: String,
    onIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(30.dp)// Slightly larger to match the premium feel
            .clip(OutlinedTextFieldDefaults.shape) // More rounded
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                color = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                shape = OutlinedTextFieldDefaults.shape
            )
            .clickable(
                onClick = onIconClick
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "$name Icon",
            modifier = Modifier
                .size(62.dp),
            tint = Color.Unspecified
        )
    }


}

@Composable
private fun MedicationValidaty(
    manufacturedDate: String,
    expiryDate: String,
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)// Slightly larger to match the premium feel
                .clip(OutlinedTextFieldDefaults.shape) // More rounded
                .border(
                    width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                    color = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                    shape = OutlinedTextFieldDefaults.shape
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Manufacturing", style = MaterialTheme.typography.labelMedium)
                Text(
                    manufacturedDate,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Expiry", style = MaterialTheme.typography.labelMedium)
                Text(
                    expiryDate,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (false)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }


}

@Composable
private fun MedicationCureInfo(
    cures: List<String>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cures.forEach {
         SuggestionChip(
                label = {
                    Text(text = it, style = MaterialTheme.typography.labelMedium)
                },
                onClick = {},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }
    }
}

@Composable
private fun MedicationSideEffects(sideEffects: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sideEffects.forEach {
           FilterChip(
                label = {
                    Text(text = it, style = MaterialTheme.typography.labelMedium)
                },
                onClick = {},
                selected = false,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                    labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            )
        }
    }
}

@Composable
private fun MedicationPrecautions(precautions: List<String>) {
    val dimension = LocalDimensions.current
    FlowColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        precautions.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularBullet()
                Spacer(Modifier.width(dimension.dimen4dp))
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun MedicationInstructions(
    instructions: List<String>
) {
    val dimension = LocalDimensions.current
    Column(verticalArrangement = Arrangement.spacedBy(dimension.dimen4dp)) {
        instructions.forEach { effect ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularOutlineBullet()
                Spacer(Modifier.width(dimension.dimen4dp))
                Text(effect, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@CommonPreview
@Composable
private fun PreviewMedicationComponentsr() {
    MedyoTheme() {
        FlowColumn(
            modifier = Modifier.padding(10.dp),
        ) {
            MedicationHeader()
            MedicationCureInfo(cures = listOf("Dizzinesss", "Body Pain", "Headache 3"))
            MedicationSideEffects(sideEffects = listOf("Dizzinesss", "Body Pain", "Headache 3"))
            MedicationPrecautions(precautions = listOf("Dizzinesss", "Body Pain", "Headache 3"))
            MedicationInstructions(
                instructions = listOf(
                    "Take 1 tablet every 2 hours",
                    "Take 1 tablet every 2 hours"
                )
            )

            MedicationValidaty(
                manufacturedDate = "01 Jan 2024",
                expiryDate = "01 Jan 2027"
            )
        }
    }
}

@CommonPreview
@Composable
private fun PreviewMedicationDetailScreen() {
    MedyoTheme() {
        MedicationDetailContent()
    }
}
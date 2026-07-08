package medyo.com.expiry_dashboard.impl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import medyo.com.core.database.entity.MedicationEntity
import medyo.com.core.design_system.theme.icon.MedyoIcons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryDashboardScreen(
    onBackClick: () -> Unit,
    viewModel: ExpiryDashboardViewModel = hiltViewModel()
) {
    val medications by viewModel.medicationsWithExpiry.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expiry Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = MedyoIcons.ArrowBack.icon, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (medications.isEmpty()) {
            Text(
                text = "No expiring medications in stock.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(medications) { medication ->
                    MedicationExpiryItem(medication)
                }
            }
        }
    }
}

@Composable
fun MedicationExpiryItem(medication: MedicationEntity) {
    val isExpired = medication.expiryDate?.let { it < System.currentTimeMillis() } ?: false
    val containerColor = if (isExpired) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isExpired) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = medication.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Strength: ${medication.dosageStrength}")
            
            medication.expiryDate?.let { timestamp ->
                val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isExpired) "Expired on: $dateStr" else "Expires on: $dateStr",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

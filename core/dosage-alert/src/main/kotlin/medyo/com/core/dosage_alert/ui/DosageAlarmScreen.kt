package medyo.com.core.dosage_alert.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.icon.MedyoIcons
import medyo.com.core.design_system.utils.compose.CommonPreview
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DosageAlarmScreen(
    scheduleId: Long,
    medicationId: Long,
    scheduledTimestamp: Long,
    onDismiss: () -> Unit
) {
    // In a real implementation, we would fetch the medication details using medicationId
    // For now, we will display generic info and format the timestamp.
    
    val formatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())
    val timeString = if (scheduledTimestamp > 0) {
        formatter.format(Instant.ofEpochMilli(scheduledTimestamp))
    } else {
        "Now"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = MedyoIcons.Alert.icon,
            contentDescription = MedyoIcons.Alert.contentDescription,
            modifier = Modifier.size(120.dp),
            tint = MedyoIcons.Alert.iconTint
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Time to take your medication",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Scheduled for $timeString",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                // Log the medication as taken
                // Trigger auto-skip cancellation or update DosageHistory
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Take Medication")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = {
                // Snooze for 15 minutes
                // Reschedule alarm
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Snooze (15m)")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = {
                // Skip the medication
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Skip Dose")
        }
    }
}

@Composable
@CommonPreview
private fun DosageAlarmScreenPreview() {
    MedyoTheme {
        DosageAlarmScreen(
            scheduleId = 1L,
            medicationId = 1L,
            scheduledTimestamp = Instant.now().toEpochMilli(),
            onDismiss = {}
        )
    }
}

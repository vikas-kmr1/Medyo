package medyo.com.core.design_system.component.date_and_time_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.theme.shapes.MedyoShapes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: LocalDate?,
    onValueChange: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli()
    )

    val formattedValue = value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: ""

    Box(modifier = modifier) {
        OutlinedTextField(
            value = formattedValue,
            onValueChange = {},
            label = { Text(label) },
            placeholder = { Text("YYYY-MM-DD") },
            readOnly = true,
            isError = isError,
            supportingText = supportingText,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        // Hidden surface over the text field to intercept clicks inside the bounding box
        Surface(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 12.dp)
                .clip(OutlinedTextFieldDefaults.shape)
                .clickable { showDatePicker = true },
            color = androidx.compose.ui.graphics.Color.Transparent
        ) {}
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                        onValueChange(selectedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            shape = LocalAppShapes.current.curvedRect,
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

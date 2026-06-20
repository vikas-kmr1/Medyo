package medyo.com.feature.scanner.impl.MedicationEdit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.component.date_and_time_picker.DatePickerField
import medyo.com.core.design_system.component.dialog.FullScreenDialog
import medyo.com.core.design_system.component.textfield.MedyoTextField
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.icon.MedyoIcons
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.design_system.utils.getMedicationIcon
import medyo.com.core.utils.constants.MedicationCategory
import medyo.com.core.utils.constants.MedicationType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationEditScreen(
    uiState: MedicationEditUiState,
    onBackClick: () -> Unit = {},
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onManufacturerChange: (String) -> Unit,
    onMedicationTypeChange: (MedicationType) -> Unit,
    onCategoryChange: (MedicationCategory) -> Unit,
    onManufacturingDateChange: (LocalDate) -> Unit,
    onExpiryDateChange: (LocalDate) -> Unit,
    onDosageIntervalChange: (String) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onTotalDosesChange: (String) -> Unit,
) {
    BackHandler() { }
    val dimen = LocalDimensions.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    FullScreenDialog {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimen.dimen16dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stickyHeader {
                MedicationTopBar(
                    title = uiState.category.label,
                    onClose = onBackClick,
                    onSave = onSave
                )
            }
            item {
                MedicineCategoryChipGroup(
                    selected = uiState.category,
                    onSelected = onCategoryChange
                )
            }
            item {
                ManufactureField(
                    value = uiState.manufacturer,
                    onValueChange = onManufacturerChange
                )
            }
            item {
                MedicationNameWithIconField(
                    name = uiState.name,
                    nameError = uiState.nameError,
                    onNameChange = onNameChange,
                    medicationType = uiState.medicationType,
                    onIconClick = { showBottomSheet = true }
                )
            }
            item {
                MedicationDatesFields(
                    manufacturingDate = uiState.manufacturingDate,
                    manufacturingDateError = uiState.manufacturingDateError,
                    onManufacturingDateChange = onManufacturingDateChange,
                    expiryDate = uiState.expiryDate,
                    expiryDateError = uiState.expiryDateError,
                    onExpiryDateChange = onExpiryDateChange
                )
            }
            item {
                DosageIntervalField(
                    value = uiState.dosageIntervalMinutes,
                    error = uiState.dosageIntervalError,
                    onValueChange = onDosageIntervalChange
                )
            }
            item {
                TreatmentDatesFields(
                    startDate = uiState.startDate,
                    startDateError = uiState.startDateError,
                    onStartDateChange = onStartDateChange,
                    endDate = uiState.endDate,
                    endDateError = uiState.endDateError,
                    onEndDateChange = onEndDateChange
                )
            }
            item {
                TotalDosesField(
                    value = uiState.totalDoses,
                    error = uiState.totalDosesError,
                    onValueChange = onTotalDosesChange
                )
            }
        }

        if (showBottomSheet) {
            MedicationTypeBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                onTypeSelected = { type ->
                    onMedicationTypeChange(type)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
private fun ManufactureField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MedyoTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Manufacture",
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun MedicationNameWithIconField(
    name: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    medicationType: MedicationType,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimen = LocalDimensions.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(dimen.dimen8dp),
        verticalAlignment = Alignment.Bottom
    ) {
        MedicationIcon(
            iconRes = getMedicationIcon(medicationType),
            name = medicationType.name.lowercase(),
            onIconClick = onIconClick
        )

        MedyoTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Medication Name",
            modifier = Modifier.weight(1f),
            isError = nameError != null,
            supportingText = nameError?.let { { Text(it) } }
        )
    }
}

@Composable
private fun MedicationDatesFields(
    modifier: Modifier = Modifier,
    manufacturingDate: LocalDate?,
    manufacturingDateError: String?,
    onManufacturingDateChange: (LocalDate) -> Unit,
    expiryDate: LocalDate?,
    expiryDateError: String?,
    onExpiryDateChange: (LocalDate) -> Unit
) {
    val dimen = LocalDimensions.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimen.dimen8dp)
    ) {
        DatePickerField(
            value = manufacturingDate,
            onValueChange = onManufacturingDateChange,
            label = "Mfg Date",
            modifier = Modifier.weight(1f),
            isError = manufacturingDateError != null,
            supportingText = manufacturingDateError?.let { { Text(it) } }
        )
        DatePickerField(
            value = expiryDate,
            onValueChange = onExpiryDateChange,
            label = "Expiry Date",
            modifier = Modifier.weight(1f),
            isError = expiryDateError != null,
            supportingText = expiryDateError?.let { { Text(it) } }
        )
    }
}

@Composable
private fun DosageIntervalField(
    modifier: Modifier = Modifier,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    MedyoTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Dosage Interval (in minutes)",
        placeholder = "e.g. 480 for 8 hours",
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun TreatmentDatesFields(
    modifier: Modifier = Modifier,
    startDate: LocalDate?,
    startDateError: String?,
    onStartDateChange: (LocalDate) -> Unit,
    endDate: LocalDate?,
    endDateError: String?,
    onEndDateChange: (LocalDate) -> Unit
) {
    val dimen = LocalDimensions.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimen.dimen8dp)
    ) {
        DatePickerField(
            value = startDate,
            onValueChange = onStartDateChange,
            label = "Start Date",
            modifier = Modifier.weight(1f),
            isError = startDateError != null,
            supportingText = startDateError?.let { { Text(it) } }
        )

        DatePickerField(
            value = endDate,
            onValueChange = onEndDateChange,
            label = "End Date",
            modifier = Modifier.weight(1f),
            isError = endDateError != null,
            supportingText = endDateError?.let { { Text(it) } }
        )
    }
}

@Composable
private fun TotalDosesField(
    modifier: Modifier = Modifier,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    MedyoTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Total Doses Prescribed",
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun MedicationIcon(
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.background,
    iconRes: Int,
    name: String,
    onIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(4.dp)// Slightly larger to match the premium feel
            .clip(OutlinedTextFieldDefaults.shape) // More rounded
            .background(iconBackgroundColor.copy(alpha = 0.25f))
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
                .size(52.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = MedyoIcons.ArrowDropDown.icon,
            contentDescription = MedyoIcons.ArrowDropDown.contentDescription
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationTypeBottomSheet(
    sheetState: androidx.compose.material3.SheetState,
    onDismissRequest: () -> Unit,
    onTypeSelected: (MedicationType) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { },
        shape = LocalAppShapes.current.bottomSheetShape
    ) {
        Text(
            text = "Select Medication Type",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(MedicationType.entries) { type ->
                MedicationTypeGridItem(
                    type = type,
                    onClick = { onTypeSelected(type) }
                )
            }
        }
    }
}

@Composable
private fun MedicationTypeGridItem(
    type: MedicationType,
    onClick: () -> Unit
) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = getMedicationIcon(type)),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color.Unspecified
        )
        Text(
            text = type.label,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}

@Composable
private fun MedicineCategoryChipGroup(
    selected: MedicationCategory,
    onSelected: (MedicationCategory) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        MedicationCategory.entries.forEach { category ->
            FilterChip(
                modifier = Modifier
                    .weight(.1f)
                    .padding(horizontal = 8.dp),
                selected = selected == category,
                onClick = { onSelected(category) },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected == category,
                    borderColor = MaterialTheme.colorScheme.outline,
                    selectedBorderColor = MaterialTheme.colorScheme.primary,

                ),
                label = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), text = category.label,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}

@Composable
private fun MedicationTopBar(
    title: String,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onClose) {
            Icon(
                imageVector = MedyoIcons.Close.icon,
                contentDescription = MedyoIcons.Close.contentDescription
            )
        }
        Text(
            modifier = Modifier.weight(1f), text = title,
            style = MaterialTheme.typography.bodyLarge
        )

        TextButton(onClick = onSave) {
            Text(text = "save")
        }

    }
}

@CommonPreview
@Composable
private fun MedicationTopbarPreveiw() {
    MedyoTheme() {
        MedicationTopBar(
            title = "Meidcation Name",
            {},
            {}
        )
    }
}

@CommonPreview
@Composable
private fun MedicationEditScreenPreview() {
    MedyoTheme {
        MedicationEditScreen(
            uiState = MedicationEditUiState(),
            onBackClick = {},
            onSave = {},
            onNameChange = {},
            onManufacturerChange = {},
            onMedicationTypeChange = {},
            onCategoryChange = {},
            onManufacturingDateChange = {},
            onExpiryDateChange = {},
            onDosageIntervalChange = {},
            onStartDateChange = {},
            onEndDateChange = {},
            onTotalDosesChange = {}
        )
    }
}
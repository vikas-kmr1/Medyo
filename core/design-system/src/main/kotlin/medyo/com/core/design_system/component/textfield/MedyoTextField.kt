package medyo.com.core.design_system.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import medyo.com.core.utils.kotlin.emptyString

@Composable
fun MedyoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = emptyString,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        placeholder = { Text(placeholder) },
        isError = isError,
        supportingText = supportingText
    )
}
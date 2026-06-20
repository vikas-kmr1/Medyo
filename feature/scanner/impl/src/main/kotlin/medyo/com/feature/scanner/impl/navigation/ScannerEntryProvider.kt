package medyo.com.feature.scanner.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import medyo.com.core.navigation.Navigator
import medyo.com.feature.scanner.api.ScannerNavKey
import medyo.com.feature.scanner.impl.CameraPreviewRoot
import medyo.com.feature.scanner.impl.MedicationEdit.MedicationEditScreen

fun EntryProviderScope<NavKey>.scannerEntry(navigator: Navigator) {
    entry<ScannerNavKey> { CameraPreviewRoot(onBackClick = { navigator.goBack() })
    }
}

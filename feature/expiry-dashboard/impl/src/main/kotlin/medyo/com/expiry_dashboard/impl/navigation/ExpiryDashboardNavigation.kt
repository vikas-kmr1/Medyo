package medyo.com.expiry_dashboard.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import medyo.com.core.navigation.Navigator
import medyo.com.expiry_dashboard.api.ExpiryDashboardNavKey
import medyo.com.expiry_dashboard.impl.ExpiryDashboardScreen

fun EntryProviderScope<NavKey>.expiryDashboardEntry(navigator: Navigator) {
    entry<ExpiryDashboardNavKey> {
        ExpiryDashboardScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}

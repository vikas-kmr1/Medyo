package medyo.com.core.design_system.utils.compose

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalResources

@Composable
fun PluralResource(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any? = emptyArray()
): String {
    return LocalResources.current
        .getQuantityString(resId, quantity, *formatArgs)
}




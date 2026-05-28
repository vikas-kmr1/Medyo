package medyo.com.core.design_system.theme.shapes


import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp



@Immutable
data class ExtendedShapes(
    val cardShape: Shape,
    val bottomSheetShape: Shape,
    val buttonShape: Shape,
    val chipShape: Shape,
    val textFieldShape: Shape,
    val variantShape: Shape,
    val imageShape: Shape,
    val pillShape: Shape,
    val dialogShape: Shape,
    val curvedRect: Shape,
)



/**
 * An instance of the custom [ExtendedShapes] class with specific rounded corner shapes for cards
 * and bottom sheets.
 */
val appShapes =
    ExtendedShapes(
        cardShape = RoundedCornerShape(24.dp),
        bottomSheetShape = RectangleShape,
        buttonShape = RoundedCornerShape(24.dp),
        chipShape = RoundedCornerShape(16.dp),
        textFieldShape = RoundedCornerShape(16.dp),
        variantShape = RoundedCornerShape(8.dp),
        imageShape = RoundedCornerShape(8.dp),
        pillShape = RoundedCornerShape(30.dp),
        dialogShape = RoundedCornerShape(32.dp),
        curvedRect = RoundedCornerShape(5.dp)
    )

// Map ExtendedShapes to Material3 Shapes
val MedyoShapes = Shapes(
    extraSmall = appShapes.variantShape as CornerBasedShape,
    small = appShapes.chipShape as CornerBasedShape,
    medium = appShapes.textFieldShape as CornerBasedShape,
    large = appShapes.buttonShape as CornerBasedShape,
    extraLarge = appShapes.cardShape as CornerBasedShape,
)


/**
 * A [CompositionLocal] to store the custom shapes, making them accessible throughout the Compose
 * hierarchy.
 */
val LocalAppShapes = compositionLocalOf { appShapes }
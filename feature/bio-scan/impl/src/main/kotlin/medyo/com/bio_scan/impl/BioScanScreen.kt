package medyo.com.bio_scan.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import medyo.com.core.design_system.component.button.BioScanWidget
import medyo.com.core.design_system.component.card.MedicationCardItemData
import medyo.com.core.design_system.component.scrollbar.DraggableScrollbar
import medyo.com.core.design_system.component.scrollbar.rememberDraggableScroller
import medyo.com.core.design_system.component.scrollbar.scrollbarState
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.design_system.utils.getMedicationIcon
import medyo.com.core.ui.MedicationCardList
import medyo.com.core.utils.constants.MedicationType

@Composable
internal fun BioScanScreen(
    onBioScanClick: () -> Unit,
    onMedicationClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BioScanViewmodel = hiltViewModel()
) {
    val context = LocalContext.current
    // 1. STATE MANAGEMENT: Track the scrolling of the medications list
    val lazyListState = rememberLazyListState()

    // 2. SCROLL DETECTION: Check if the user has scrolled the list at all
    val isScrolled by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0
        }
    }

    val scannedMedications by viewModel.scannedMedicationsList.collectAsStateWithLifecycle()
    val itemsAvailable = scannedMedications.size

    val scrollbarState = lazyListState.scrollbarState(
        itemsAvailable = itemsAvailable,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        // 3. COLLAPSIBLE TOP WIDGET: Fade out and shrink vertically when scrolled
        AnimatedVisibility(
            visible = !isScrolled,
            enter = expandVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                )
            ) + fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ),
            exit = shrinkVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                )
            ) + fadeOut(
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            )
        ) {
            BioScanWidget(
                modifier = Modifier.padding(horizontal = 24.dp),
                onClick = onBioScanClick,
            )
        }

        if (scannedMedications.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = "Today's Medications",
                style = MaterialTheme.typography.titleLarge
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
            ) {
                // 4. SELF-EXPANDING LIST: The LazyColumn naturally fills all available remaining height
                //    (weight = 1f) and dynamically expands to full-screen height when the top widget collapses!
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    state = lazyListState,
                ) {
                    MedicationCardList(
                        MedicationItems = scannedMedications.map {
                            val colors = getMedicationColors(
                                index = it.medicationId.toInt() % colorOptions.size
                            )
                            MedicationCardItemData(
                                id = it.medicationId,
                                name = it.brand, // Note: Same name but different ID and Icon
                                dosage = "10mg",
                                iconRes = getMedicationIcon(
                                    if (it.form.isBlank()) MedicationType.OTHER else MedicationType.valueOf(
                                        it.form
                                    )
                                ),
                                iconBackgroundColor = colors.iconBackgroundColor, // Using hardcoded or MaterialTheme colors
                                cardGradientStartColor = colors.cardGradientStartColor,
                                shadowColor = colors.shadowColor,
                            )
                        },
                        onCardClick = onMedicationClick
                    )

                    if (itemsAvailable > 0) {
                        item {
                            // 5. BOTTOM SPACER: Ensures the list is always scrollable,
                            // even with few items, so the top widget can collapse!
                            Spacer(modifier = Modifier.fillParentMaxHeight(0.5f))
                        }
                    }
                }


                lazyListState.DraggableScrollbar(
                    modifier = Modifier
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .padding(horizontal = 2.dp)
                        .align(Alignment.CenterEnd),
                    state = scrollbarState,
                    orientation = Orientation.Vertical,
                    onThumbMoved = lazyListState.rememberDraggableScroller(
                        itemsAvailable = itemsAvailable,
                    ),
                )
            }
        }
    }
}

private data class MedicationBgColor(
    val iconBackgroundColor: Color = Color.White,
    val cardGradientStartColor: Color = Color.White,
    val shadowColor: Color = Color.Black
)

private   val colorOptions = listOf(
    MedicationBgColor(
        iconBackgroundColor = Color(0xFFE3F2FD), // Light Blue
        cardGradientStartColor = Color(0xFFBBDEFB),
        shadowColor = Color(0x402196F3)
    ),
    MedicationBgColor(
        iconBackgroundColor = Color(0xFF00ACC1),
        cardGradientStartColor = Color(0xFF4DD0E1),
        shadowColor = Color(0xFF00ACC1)
    ),
    MedicationBgColor(
        iconBackgroundColor = Color(0xFFFF9800),
        cardGradientStartColor = Color(0xFFFFB74D),
        shadowColor = Color(0xFFFF9800)
    ),
    MedicationBgColor(
        iconBackgroundColor = Color.White,
        cardGradientStartColor = Color.White,
        shadowColor = Color.Black,
    ),
    MedicationBgColor(
        iconBackgroundColor = Color(0xFFE57373),
        cardGradientStartColor = Color(0xFFEF9A9A),
        shadowColor = Color(0xFFE57373)
    )
)
private fun getMedicationColors(index: Int = 0): MedicationBgColor {

    return  colorOptions.getOrElse(index, defaultValue = { colorOptions.random()})
}


@Composable
@CommonPreview
private fun BioScanScreenPreview() {
    MedyoTheme {
        BioScanScreen(onBioScanClick = {},
            {})
    }
}
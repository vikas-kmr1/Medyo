package medyo.com.bio_scan.impl

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import medyo.com.core.design_system.component.button.BioScanWidget
import medyo.com.core.design_system.component.scrollbar.DraggableScrollbar
import medyo.com.core.design_system.component.scrollbar.rememberDraggableScroller
import medyo.com.core.design_system.component.scrollbar.scrollbarState
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.core.ui.MedicationCardList
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import medyo.com.core.design_system.component.card.MedicationCardItemData
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.utils.getMedicationIcon
import medyo.com.core.utils.constants.MedicationType

@Composable
internal fun BioScanScreen(
    onBioScanClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BioScanViewmodel = hiltViewModel()
) {
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
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 3. COLLAPSIBLE TOP WIDGET: Fade out and shrink vertically when scrolled
        AnimatedVisibility(
            visible = !isScrolled,
            enter = expandVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy
                )
            ) + fadeIn(),
            exit = shrinkVertically(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) + fadeOut()
        ) {
            BioScanWidget(
                modifier = Modifier.padding(horizontal = 24.dp),
                onClick = onBioScanClick,
            )
        }

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
                modifier = Modifier.padding(horizontal = LocalDimensions.current.dimen24dp),
                state = lazyListState,
            ) {
                MedicationCardList(
                    MedicationItems = scannedMedications.map {
                        MedicationCardItemData(
                            id = it.medicationId.toInt(),
                            name = it.brand, // Note: Same name but different ID and Icon
                            dosage = "10mg",
                            iconRes = getMedicationIcon(MedicationType.INJECTION),
                            iconBackgroundColor = Color.White, // Using hardcoded or MaterialTheme colors
                            cardGradientStartColor = Color.White,
                            shadowColor = Color.Black
                        )
                    },
                )
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

//
//private fun feedItemsSize(
//    feedState: NewsFeedUiState,
//    onboardingUiState: OnboardingUiState,
//): Int {
//    val feedSize = when (feedState) {
//        NewsFeedUiState.Loading -> 0
//        is NewsFeedUiState.Success -> feedState.feed.size
//    }
//    val onboardingSize = when (onboardingUiState) {
//        OnboardingUiState.Loading,
//        OnboardingUiState.LoadFailed,
//        OnboardingUiState.NotShown,
//            -> 0
//
//        is OnboardingUiState.Shown -> 1
//    }
//    return feedSize + onboardingSize
//}

@Composable
@CommonPreview
private fun BioScanScreenPreview() {
    MedyoTheme {
        BioScanScreen(onBioScanClick = {})
    }
}
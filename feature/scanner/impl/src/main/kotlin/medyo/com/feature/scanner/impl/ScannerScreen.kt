package medyo.com.feature.scanner.impl

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import medyo.com.core.design_system.component.lottie.AiProgressLottieAnimation
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.icon.MedyoIcons
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.utils.compose.CommonPreview
import medyo.com.feature.scanner.impl.MedicationEdit.MedicationEditScreen
import medyo.com.feature.scanner.impl.MedicationEdit.MedicationEditViewModel
import java.util.UUID

@Composable
internal fun CameraPreviewRoot(
    onBackClick: () -> Unit,
    viewModel: ScannerViewModel = hiltViewModel(),
    medicationEditViewModel: MedicationEditViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val medicationUiState = medicationEditViewModel.uiState.collectAsStateWithLifecycle()
    val dbWrite = medicationEditViewModel.dbWriteState
    when (val state = uiState.value) {
        ScannerUiState.Idle -> CameraPreviewScreen(
            viewModel = viewModel,
            onBackClick = onBackClick
        )

        ScannerUiState.Loading -> AiProgressLottieAnimation()
        is ScannerUiState.Error -> {}
        is ScannerUiState.Success -> {
            LaunchedEffect(state.medicationInfo) {
                medicationEditViewModel.onInit(state.medicationInfo)
            }
            if (viewModel.showEditMedicationDialog) {
                MedicationEditScreen(
                    uiState = medicationUiState.value,
                    onBackClick = viewModel::closeEditMedicationDialog,
                    onSave = medicationEditViewModel::onSave,
                    onNameChange = medicationEditViewModel::onNameChange,
                    onManufacturerChange = medicationEditViewModel::onManufacturerChange,
                    onMedicationTypeChange = medicationEditViewModel::onMedicationTypeChange,
                    onCategoryChange = medicationEditViewModel::onCategoryChange,
                    onManufacturingDateChange = medicationEditViewModel::onManufacturingDateChange,
                    onExpiryDateChange = medicationEditViewModel::onExpiryDateChange,
                    onDosageIntervalChange = medicationEditViewModel::onDosageIntervalChange,
                    onStartDateChange = medicationEditViewModel::onStartDateChange,
                    onEndDateChange = medicationEditViewModel::onEndDateChange,
                    onTotalDosesChange = medicationEditViewModel::onTotalDosesChange,
                    onStockQuantityChange = medicationEditViewModel::onStockQuantityChange,
                    onAlertDaysBeforeExpiryChange = medicationEditViewModel::onAlertDaysBeforeExpiryChange,
                    onDosageTimeAdd = medicationEditViewModel::onDosageTimeAdd,
                    onDosageTimeRemove = medicationEditViewModel::onDosageTimeRemove
                )
            }
        }
    }

    LaunchedEffect(dbWrite) {
        if(dbWrite){
            onBackClick()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraPreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    // Bind system bar visibility lifecycle to this Composable screen
    DisposableEffect(context, view) {
        val window = (context as? Activity)?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)

            // Allow transient system bars to overlay temporarily when user swipes
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Fully hide both status and navigation bars for scanner focus
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        onDispose {
            // Restore system bars immediately when exiting the scanner screen
            val window = (context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(
            onBackClick = onBackClick,
            modifier = modifier,
            viewModel = viewModel
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                "Whoops! Looks like we need your camera to work our magic!" +
                        "Don't worry, we just wanna see your pretty face (and maybe some cats).  " +
                        "Grant us permission and let's get this party started!"
            } else {
                "Hi there! We need your camera to work our magic! ✨\n" +
                        "Grant us permission and let's get this party started! \uD83C\uDF89"
            }
            Text(textToShow, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Unleash the Camera!")
            }
        }
    }
}

@Composable
private fun CameraPreviewContent(
    onBackClick: () -> Unit,
    viewModel: ScannerViewModel,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val capturedImages = viewModel.capturedImages
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context, lifecycleOwner)
    }

    var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }

    val autofocusRequestId = autofocusRequest.first
    val showAutofocusIndicator = autofocusRequest.second.isSpecified
    val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

    if (showAutofocusIndicator) {
        LaunchedEffect(autofocusRequestId) {
            delay(1000)
            autofocusRequest = autofocusRequestId to Offset.Unspecified
        }
    }

    var torchEnabled by remember { mutableStateOf(false) }

    surfaceRequest?.let { request ->
        val coordinateTransformer = remember { MutableCoordinateTransformer() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // 1. Camera viewfinder
            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = coordinateTransformer,
                modifier = modifier.pointerInput(viewModel, coordinateTransformer) {
                    detectTapGestures { tapCoords ->
                        with(coordinateTransformer) {
                            viewModel.tapToFocus(tapCoords.transform())
                        }
                        autofocusRequest = UUID.randomUUID() to tapCoords
                    }
                }
            )


            // 4. Instructional Badge
            val infiniteBadgeTransition = rememberInfiniteTransition(label = "badgePulse")
            val badgeAlpha by infiniteBadgeTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "badgeAlpha"
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 110.dp)
                    .graphicsLayer(alpha = badgeAlpha)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF00F2FE), CircleShape)
                    )
                    Text(
                        text = "ALIGN MEDICATION WITHIN FRAME",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 5. Immersive Top App Bar
            CameraTopAppBar(
                onBackClick = onBackClick,
                torchEnabled = torchEnabled,
                onTorchToggle = {
                    torchEnabled = !torchEnabled
                    viewModel.toggleTorch(torchEnabled)
                },
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // 6. Bottom Controls Panel
            CameraBottomControls(
                capturedImages = capturedImages,
                onCapture = { viewModel.captureImage(context) },
                onRemove = viewModel::onRemoveImage,
                onProcess = viewModel::onProceed,
                modifier = Modifier.align(Alignment.BottomCenter),
                onClear = viewModel::onAllClear
            )

            // 7. Autofocus Tap Indicator
            AnimatedVisibility(
                visible = showAutofocusIndicator,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .offset { autofocusCoords.takeOrElse { Offset.Zero }.round() }
                    .offset((-24).dp, (-24).dp)
            ) {
                val focusScale = remember(autofocusRequestId) { Animatable(1.5f) }
                LaunchedEffect(autofocusRequestId) {
                    focusScale.animateTo(
                        targetValue = 1.0f,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                Spacer(
                    Modifier
                        .graphicsLayer(scaleX = focusScale.value, scaleY = focusScale.value)
                        .border(2.dp, Color.White, CircleShape)
                        .size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun CameraTopAppBar(
    onBackClick: () -> Unit,
    torchEnabled: Boolean,
    onTorchToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(
                vertical = WindowInsets.statusBars.asPaddingValues()
                    .calculateTopPadding() + 32.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
        ) {
            Icon(
                imageVector = MedyoIcons.ArrowBack.icon,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color(0xFF00FF87), CircleShape)
            )
            Text(
                text = "LIVE CAMERA",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )
        }


        IconButton(
            onClick = onTorchToggle,
            modifier = Modifier
        ) {
            Icon(
                imageVector = if (torchEnabled) MedyoIcons.FlashOn.icon else MedyoIcons.FlashOff.icon,
                contentDescription = "Toggle Torch",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CameraBottomControls(
    capturedImages: List<Bitmap>,
    onCapture: () -> Unit,
    onRemove: (Int) -> Unit,
    onClear: () -> Unit,
    onProcess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current
    val shape = LocalAppShapes.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 24.dp,
                top = 24.dp,
                start = 20.dp,
                end = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.12f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val dotTransition = rememberInfiniteTransition(label = "pulseDot")
                val dotAlpha by dotTransition.animateFloat(
                    initialValue = 0.4f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dotAlpha"
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .graphicsLayer(alpha = dotAlpha)
                        .background(
                            if (capturedImages.size >= 5) Color(0xFFFF4B4B) else Color(
                                0xFF00FF87
                            ),
                            CircleShape
                        )
                )
                Text(
                    text = "${capturedImages.size} / 5 CAPTURED",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        AnimatedVisibility(
            visible = capturedImages.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
            ) {
                itemsIndexed(capturedImages) { index, bitmap ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(shape.curvedRect)
                            .border(1.5.dp, Color.White.copy(alpha = 0.25f), shape.curvedRect)
                    ) {

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            text = "${index + 1}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )

                        IconButton(
                            onClick = { onRemove(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(20.dp)
                                .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        ) {
                            Icon(
                                imageVector = MedyoIcons.Close.icon,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (capturedImages.isNotEmpty()) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.06f), CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = MedyoIcons.Close.icon,
                            contentDescription = "Clear All",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val isFull = capturedImages.size >= 5

                IconButton(
                    onClick = onCapture,
                    enabled = !isFull,
                    modifier = Modifier.size(84.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 3.dp,
                                    color = if (isFull) Color.White.copy(alpha = 0.3f) else Color(
                                        0xFF00F2FE
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Spacer(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isFull) {
                                        androidx.compose.ui.graphics.SolidColor(
                                            Color.White.copy(
                                                alpha = 0.2f
                                            )
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(Color.White, Color(0xFFE2E8F0))
                                        )
                                    }
                                )
                        )
                        if (isFull) {
                            Icon(
                                imageVector = MedyoIcons.Close.icon,
                                contentDescription = "Camera Full",
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                val isProcessable = capturedImages.isNotEmpty()

                IconButton(
                    onClick = onProcess,
                    enabled = isProcessable,
                    modifier = Modifier
                        .background(
                            if (isProcessable) Color(0xFF00F2FE) else Color.White.copy(alpha = 0.05f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isProcessable) Color.Transparent else Color.White.copy(alpha = 0.08f),
                            CircleShape
                        )
                        .size(54.dp)
                ) {
                    Icon(
                        imageVector = MedyoIcons.Check.icon,
                        contentDescription = "Process Items",
                        tint = if (isProcessable) Color.Black else Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@CommonPreview
@Composable
private fun PreviewCameraTopAppBar() {
    MedyoTheme {
        CameraTopAppBar(
            onBackClick = {},
            torchEnabled = false,
            onTorchToggle = {}
        )
    }
}

@CommonPreview
@Composable
private fun PreviewCameraBottomControls() {
    MedyoTheme {
        CameraBottomControls(
            capturedImages = emptyList(),
            onCapture = {},
            onRemove = {},
            onProcess = {},
            onClear = {}
        )
    }
}

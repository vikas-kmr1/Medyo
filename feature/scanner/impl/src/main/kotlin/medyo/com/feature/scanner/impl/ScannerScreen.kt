package medyo.com.feature.scanner.impl

import android.Manifest
import android.graphics.Bitmap
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import medyo.com.core.design_system.theme.LocalDimensions
import medyo.com.core.design_system.theme.MedyoTheme
import medyo.com.core.design_system.theme.icon.MedyoIcons
import medyo.com.core.design_system.theme.shapes.LocalAppShapes
import medyo.com.core.design_system.utils.compose.CommonPreview
import java.util.UUID


@Composable
internal fun ScannerScreen() {
    Column(
        content = {}
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraPreviewScreen(modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(modifier = modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Whoops! Looks like we need your camera to work our magic!" +
                        "Don't worry, we just wanna see your pretty face (and maybe some cats).  " +
                        "Grant us permission and let's get this party started!"
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
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
    viewModel: ScannerViewModel = hiltViewModel(),
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
    // Show the autofocus indicator if the offset is specified
    val showAutofocusIndicator = autofocusRequest.second.isSpecified
    // Cache the initial coords for each autofocus request
    val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

    // Queue hiding the request for each unique autofocus tap
    if (showAutofocusIndicator) {
        LaunchedEffect(autofocusRequestId) {
            delay(1000)
            // Clear the offset to finish the request and hide the indicator
            autofocusRequest = autofocusRequestId to Offset.Unspecified
        }
    }


    surfaceRequest?.let { request ->
        val coordinateTransformer = remember { MutableCoordinateTransformer() }
        Box(modifier = Modifier.fillMaxSize()) {
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

            AnimatedVisibility(
                visible = showAutofocusIndicator,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .offset { autofocusCoords.takeOrElse { Offset.Zero }.round() }
                    .offset((-24).dp, (-24).dp)
            ) {
                Spacer(
                    Modifier
                        .border(2.dp, Color.White, CircleShape)
                        .size(48.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                )
        ) {
            // Overlay elements
            Text(
                "${capturedImages.size}/5 Captured",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                CameraBottomControls(
                    capturedImages = capturedImages,
                    onCapture = { viewModel.captureImage(context) },
                    onRemove = viewModel::onRemoveImage,
                    onProcess = {}
                )
            }
        }

    }
}

@Composable
private fun CameraTopAppBar() {


}

@CommonPreview
@Composable
private fun PreviewCameraTopAppBar() {
    MedyoTheme {
        CameraTopAppBar()
    }
}

@Composable
private fun CameraBottomControls(
    capturedImages: List<Bitmap>,
    onCapture: () -> Unit,
    onRemove: (Int) -> Unit,
    onProcess: () -> Unit,
) {
    val dimensions = LocalDimensions.current
    val shape = LocalAppShapes.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                vertical = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                horizontal = dimensions.dimen16dp
            ),
    ) {
        if (capturedImages.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensions.dimen8dp),
                modifier = Modifier.padding(bottom = dimensions.dimen8dp)
            ) {
                itemsIndexed(capturedImages) { index, bitmap ->
                    Box {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(dimensions.dimen70dp)
                                .clip(shape.curvedRect)
                        )
                        IconButton(
                            onClick = { onRemove(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(dimensions.dimen24dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                MedyoIcons.Close.icon,
                                "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(dimensions.dimen16dp)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onCapture,
                enabled = capturedImages.size < 5,
                modifier = Modifier
                    .weight(1f)
                    .height(dimensions.dimen48dp)
            ) {
                Text("Capture Photo")
            }
            Spacer(modifier = Modifier.width(dimensions.dimen70dp))
            FilledTonalButton(
                onClick = onProcess,
                enabled = capturedImages.isNotEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .height(dimensions.dimen48dp)
            ) {
                Icon(MedyoIcons.Check.icon, contentDescription = null)
                Spacer(modifier = Modifier.width(dimensions.dimen8dp))
                Text("Process Items")
            }
        }
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
            onProcess = {}
        )
    }
}

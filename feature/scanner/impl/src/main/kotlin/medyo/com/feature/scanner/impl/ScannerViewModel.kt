package medyo.com.feature.scanner.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import medyo.com.core.domain.usecase.ScanAndSaveMedicineUseCase
import java.util.concurrent.Executors
import javax.inject.Inject

private val MAX_CAPTURE_ATTEMPTS = 5

sealed interface ScannerUiState  {
    data object Idle : ScannerUiState 
    data object Loading : ScannerUiState 
    data class Success(val medicineId: Long) : ScannerUiState 
    data class Error(val message: String) : ScannerUiState 
}

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanAndSaveMedicineUseCase: ScanAndSaveMedicineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState >(ScannerUiState .Idle)
    val uiState: StateFlow<ScannerUiState > = _uiState.asStateFlow()


    // Used to set up a link between the Camera and your UI.
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
    private var cameraControl: CameraControl? = null

    val cameraExecutor = Executors.newSingleThreadExecutor()

    var capturedImages by mutableStateOf(emptyList<Bitmap>())

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat()
            )
        }
    }

    private val imageCaptureUseCase =
        ImageCapture.Builder().setCaptureMode(
            ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
        ).build()


    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            imageCaptureUseCase
        )
        cameraControl = camera.cameraControl

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }

    fun toggleTorch(enabled: Boolean) {
        cameraControl?.enableTorch(enabled)
    }

    fun captureImage(context: Context) {
        if (capturedImages.size < MAX_CAPTURE_ATTEMPTS)
            imageCaptureUseCase.takePicture(
                cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        if (capturedImages.size == MAX_CAPTURE_ATTEMPTS) return
                        val bitmap = imageProxyToBitmap(image)

                        // Need to update state on main thread
                        context.mainExecutor.execute {
                            capturedImages = capturedImages + bitmap
                        }
                        image.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("ScanScreen", "Photo capture failed: ${exception.message}", exception)
                        context.mainExecutor.execute {
                            // Show error via viewModel or local state
                        }
                    }
                }
            )
    }

    fun onRemoveImage(index: Int) {
        capturedImages = capturedImages.toMutableList().apply { removeAt(index) }
    }

    fun tapToFocus(tapCoords: Offset) {
        val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }

    fun onProceed() {
        viewModelScope.launch {
            analyzeImage(capturedImages)
        }
    }


    private fun analyzeImage(images: List<Bitmap>) {
        _uiState.value = ScannerUiState .Loading
        viewModelScope.launch {
            val result = scanAndSaveMedicineUseCase.invoke(images)
            result.onSuccess { medicationId ->
                // Observe the DB flow via UseCase
                _uiState.value = ScannerUiState .Success(medicationId)

            }
                .onFailure { error ->
                    _uiState.value =
                        ScannerUiState .Error(error.message ?: "Unknown error occurred.")
                }
        }
    }


    // Convert ImageProxy to Bitmap handling rotation
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val matrix = Matrix()
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }


}

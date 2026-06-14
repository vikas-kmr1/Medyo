package medyo.com.core.ai_logic
import android.graphics.Bitmap
import medyo.com.core.ai_logic.dto.AiMedicationResponseDto

interface GeminiAiDataSource {
    suspend fun generateContext(images: List<Bitmap>): AiMedicationResponseDto?
}
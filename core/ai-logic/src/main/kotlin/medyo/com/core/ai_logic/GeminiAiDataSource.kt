package medyo.com.core.ai_logic
import android.graphics.Bitmap
import medyo.com.core.ai_logic.dto.AiMedicineResponseDto

interface GeminiAiDataSource {
    suspend fun generateContext(images: List<Bitmap>): AiMedicineResponseDto?
}
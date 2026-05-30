package medyo.com.core.ai_logic
import android.graphics.Bitmap

interface GeminiAiDataSource {
    suspend fun generateContext(images: List<Bitmap>): String
}
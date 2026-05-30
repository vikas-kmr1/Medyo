package medyo.com.core.domain

import android.graphics.Bitmap
import medyo.com.core.ai_logic.GeminiAiDataSource
import javax.inject.Inject

class GetAiGenerativeResultUseCase @Inject constructor(
    private val aiDataSource: GeminiAiDataSource
) {
    suspend operator fun invoke(
        images: List<Bitmap>
    ){
        aiDataSource.generateContext(images)
    }
}
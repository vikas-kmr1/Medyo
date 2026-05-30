package medyo.com.core.ai_logic

import android.graphics.Bitmap
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class GeminiAiDataSourceImpl @Inject constructor(
    private val genAI: GenerativeModel
)  : GeminiAiDataSource {
    override suspend fun generateContext(images: List<Bitmap>): String {

        val prompt = """
                DISCLAIMER: ONLY GENERATE RESPONSES FOR MEDICINES ONLY , don't entertain any other item,.
                You are a smart medical assistant. I have provided up to 5 images scanned from a medicine's packaging. 
                Analyze ALL the images to extract the following details and return them strictly in valid JSON format ONLY, without any Markdown code blocks or wrapping text:
                If you cannot find dates, try to infer them from the text (e.g. 05/2026 -> 2026-05-01). If truly not found, return null for dates.
                If brand or salts are not found, leave them blank.
                Use your best judgement to extract the medical knowledge to populate expected 'sideEffects' and 'cures' based on the identified brand or salts.
            """.trimIndent()

        val inputContent = content {
            images.forEach { bitmap ->
                image(bitmap)
            }
            text(prompt)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = genAI.generateContent(inputContent)
                Timber.tag("gemini response").d("Response: ${response.text}")
                response.text ?: "no response"
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

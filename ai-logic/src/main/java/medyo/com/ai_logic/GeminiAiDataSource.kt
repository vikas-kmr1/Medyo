package medyo.com.ai_logic

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

const val MODEL_NAME = "gemini-3.1-pro-preview"

class GeminiAiDataSource {

    private val genAi by lazy {
        Firebase.ai(backend = GenerativeBackend.vertexAI(
            location = "global",
        ))
            .generativeModel(modelName = MODEL_NAME)
    }


    suspend fun generateContext(name: String): String {

        val prompt = """
                medicine $name
                and default dates are 01/2026 and 01/2027
                You are a smart medical assistant. I have provided up to 5 images scanned from a medicine's packaging. 
                Analyze ALL the images to extract the following details and return them strictly in valid JSON format ONLY, without any Markdown code blocks or wrapping text:
                {
                  "brand": "Medicine Brand Name",
                  "salts": "Chemical compositions or salts string",
                  "mfgDate": "Jan 2026",
                  "expDate": "Feb 2027",
                  "sideEffects": ["effect 1", "effect 2"],
                  "cures": ["disease 1", "disease 2"],
                  precautions: ["precaution 1", "precaution 2"]
                  instructions: ["Empty Stomach"]
                }
                If you cannot find dates, try to infer them from the text (e.g. 05/2026 -> 2026-05-01). If truly not found, return null for dates.
                If brand or salts are not found, leave them blank.
                Use your best judgement to extract the medical knowledge to populate expected 'sideEffects' and 'cures' based on the identified brand or salts.
            """.trimIndent()

//        val inputContent = content {
//            images.forEach { bitmap ->
//                image(bitmap)
//            }
//            text(promptText)
//        }

        return withContext(Dispatchers.IO) {
            try {
                val response = genAi.generateContent(prompt)
                Timber.tag("gemini response").d("Response: ${response.text}")
                response.text ?: "no response"
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

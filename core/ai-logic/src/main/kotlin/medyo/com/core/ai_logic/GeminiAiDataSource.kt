package medyo.com.core.ai_logic

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

const val MODEL_NAME = "gemini-3.1-pro-preview"


enum class MedicineType {
    CAPSULE, TABLET, DROPS, INHALER, INJECTION, PATCH, SUSPENSION, SYRUP, VIAL, OTHER
}

class GeminiAiDataSource {
    private val config = generationConfig {
        responseMimeType = "application/json"
        // Define the schema so the LLM knows exactly what keys to generate
        responseSchema = Schema.obj(
            properties = mapOf(
                "brand" to Schema.string(),
                "salts" to Schema.string(),
                "mfgDate" to Schema.string(),
                "expDate" to Schema.string(),
                "sideEffects" to Schema.array(
                    items = Schema.string()
                ),
                "cures" to Schema.array(
                    items = Schema.string()
                ),
                "precautions" to Schema.array(
                    items = Schema.string()
                ),
                "instructions" to Schema.array(
                    items = Schema.string()
                ),
                "category" to Schema.string(),
            ),
        )
        temperature = .1f // Low temperature for factual extraction, no creativity
    }

    private val genAi by lazy {
        Firebase.ai(backend = GenerativeBackend.vertexAI(
            location = "global",
        ))
            .generativeModel(modelName = MODEL_NAME, generationConfig = config)
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
                  instructions: ["Empty Stomach"],
                  category: "Medicine Category" i.e. ${MedicineType.entries.joinToString { it.name }}
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
                val inputContent = content {
                    text(prompt)
                }
                val response = genAi.generateContent(inputContent)
                Timber.tag("gemini response").d("Response: ${response.text}")
                response.text ?: "no response"
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

package medyo.com.core.ai_logic.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.JsonSchema
import com.google.firebase.ai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.utils.constants.MedicationType
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
internal object AiLogicModule {

    const val MODEL_NAME = "gemini-3.1-flash-lite"
    const val LOCATION = "global"

    private val config = generationConfig {
        responseMimeType = "application/json"
        // Define the schema so the LLM knows exactly what keys to generate
        responseJsonSchema = JsonSchema.obj(
            properties = mapOf(
                "brand" to JsonSchema.string(),
                "salts" to JsonSchema.string(),
                "mfgDate" to JsonSchema.long(
                    description = "The date the Medication was manufactured. Must be in epoch type."
                ),
                "expDate" to JsonSchema.long(
                    description = "The date the Medication expires. Must be in epoch type."
                ),
                "sideEffects" to JsonSchema.array(items = JsonSchema.string()),
                "cures" to JsonSchema.array(items = JsonSchema.string()),
                "precautions" to JsonSchema.array(items = JsonSchema.string()),
                "instructions" to JsonSchema.array(items = JsonSchema.string()),
                "category" to JsonSchema.string(
                    nullable = false,
                    // Passing the enum values here helps the model pick the correct one
                    description = "The type of Medication. Must be one of: ${MedicationType.entries.joinToString()}"
                ),
                "errorMessage" to JsonSchema.string(nullable = true, description = "Error message if any other item scanned except Medications."),
                "statusCode" to JsonSchema.string(nullable = true, description = "Status code of the response. 200 for success, 400 for bad request, etc.")
            ),
        )
        temperature = .2f // Low temperature for factual extraction, no creativity
    }

    @Provides
    @Singleton
    fun provideFirebaseAiBackend(): GenerativeModel = Firebase.ai(
        backend = GenerativeBackend.vertexAI(location = LOCATION)
    ).generativeModel(
        modelName = MODEL_NAME, generationConfig = config,
        systemInstruction = com.google.firebase.ai.type.content {
            text("ONLY GENERATE RESPONSES FOR MedicationS ONLY, don't entertain any other item.")
        })

}
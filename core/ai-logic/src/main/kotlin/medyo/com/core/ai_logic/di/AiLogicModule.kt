package medyo.com.core.ai_logic.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.utils.constants.MedicineType
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
internal object AiLogicModule {

    const val MODEL_NAME = "gemini-3.1-flash-lite"
    const val LOCATION = "global"

    private val config = generationConfig {
        responseMimeType = "application/json"
        // Define the schema so the LLM knows exactly what keys to generate
        responseSchema = Schema.obj(
            properties = mapOf(
                "brand" to Schema.string(),
                "salts" to Schema.string(),
                "mfgDate" to Schema.string(
                    description = "The date the medicine was manufactured. Must be in the format mm-YYYY i.e May-2026 or Jun 2026."
                ),
                "expDate" to Schema.string(
                    description = "The date the medicine expires. Must be in the format mm-YYYY i.e May-2027 or Jun 2027."
                ),
                "sideEffects" to Schema.array(items = Schema.string()),
                "cures" to Schema.array(items = Schema.string()),
                "precautions" to Schema.array(items = Schema.string()),
                "instructions" to Schema.array(items = Schema.string()),
                "category" to Schema.string(
                    nullable = false,
                    // Passing the enum values here helps the model pick the correct one
                    description = "The type of medicine. Must be one of: ${MedicineType.entries.joinToString()}"
                ),
            ),
        )
        temperature = .1f // Low temperature for factual extraction, no creativity
    }

    @Provides
    @Singleton
    fun provideFirebaseAiBackend(): GenerativeModel = Firebase.ai(
        backend = GenerativeBackend.vertexAI(location = LOCATION,)
    ).generativeModel(modelName = MODEL_NAME, generationConfig = config)

}
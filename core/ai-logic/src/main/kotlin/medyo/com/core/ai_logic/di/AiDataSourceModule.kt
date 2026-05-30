package medyo.com.core.ai_logic.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.ai_logic.GeminiAiDataSource
import medyo.com.core.ai_logic.GeminiAiDataSourceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AiDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAiLogicApi(
        geminiAiDataSourceImpl: GeminiAiDataSourceImpl
    ): GeminiAiDataSource
}
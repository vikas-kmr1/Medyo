package medyo.com.logger.impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.logger.api.LoggerApi
import medyo.com.logger.impl.LoggerApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoggerModule {
    @Binds
    @Singleton
   abstract fun bindsLoogerApiImpl(
        loggerApiImpl: LoggerApiImpl,
    ): LoggerApi

}


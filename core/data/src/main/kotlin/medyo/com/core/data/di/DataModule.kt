package medyo.com.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.data.repository.BioScanRepositoryImpl
import medyo.com.core.domain.repository.BioScanRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindBioScanRepository(
        bioScanRepositoryImpl: BioScanRepositoryImpl
    ): BioScanRepository
}

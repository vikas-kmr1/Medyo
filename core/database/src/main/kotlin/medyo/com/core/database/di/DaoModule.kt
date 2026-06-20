package medyo.com.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.database.MedyoDatabase
import medyo.com.core.database.dao.MedicationDao

@InstallIn(SingletonComponent::class)
@Module
internal object DaoModule {
    @Provides
    fun provideMedicationDao(medyoDatabase: MedyoDatabase): MedicationDao =
        medyoDatabase.medicationDao()
}
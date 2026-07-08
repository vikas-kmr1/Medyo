package medyo.com.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.database.MedyoDatabase
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.dao.DosageAlertDao

@InstallIn(SingletonComponent::class)
@Module
internal object DaoModule {
    @Provides
    fun provideMedicationDao(medyoDatabase: MedyoDatabase): MedicationDao =
        medyoDatabase.medicationDao()
        
    @Provides
    fun provideDosageAlertDao(medyoDatabase: MedyoDatabase): DosageAlertDao =
        medyoDatabase.dosageAlertDao()
}
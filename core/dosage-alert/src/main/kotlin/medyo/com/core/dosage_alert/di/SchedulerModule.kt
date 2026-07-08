package medyo.com.core.dosage_alert.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import medyo.com.core.dosage_alert.scheduler.DosageAlarmScheduler
import medyo.com.core.dosage_alert.scheduler.DosageAlarmSchedulerImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SchedulerModule{
    @Binds
    abstract fun bindDosageAlarmScheduler(
        impl: DosageAlarmSchedulerImpl
    ): DosageAlarmScheduler
}


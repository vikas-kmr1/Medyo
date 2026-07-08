package medyo.com.core.notification.di

import android.app.NotificationManager
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import medyo.com.core.notification.api.Notifier
import medyo.com.core.notification.impl.NotificationChannelManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NotificationManagerModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationsModule {
    @Binds
    abstract fun bindNotifier(
        notifier: NotificationChannelManager,
    ): Notifier
}


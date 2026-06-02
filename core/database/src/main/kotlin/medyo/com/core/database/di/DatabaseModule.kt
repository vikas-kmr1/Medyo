package medyo.com.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import medyo.com.core.database.MedyoDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideMedyoDatabase(@ApplicationContext context: Context): MedyoDatabase =
        Room.databaseBuilder(
            context,
            MedyoDatabase::class.java,
            "medyo_database"
        ).build()
}
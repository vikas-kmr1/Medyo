package medyo.com.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "medyo_prefs")

@Singleton
class MedyoPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.dataStore

    companion object {
        val EXPIRY_MORNING_TIME = stringPreferencesKey("expiry_morning_time")
        val EXPIRY_EVENING_TIME = stringPreferencesKey("expiry_evening_time")
    }

    val expiryMorningTime: Flow<String> = dataStore.data.map { preferences ->
        preferences[EXPIRY_MORNING_TIME] ?: "09:00"
    }

    val expiryEveningTime: Flow<String> = dataStore.data.map { preferences ->
        preferences[EXPIRY_EVENING_TIME] ?: "18:00"
    }

    suspend fun setExpiryMorningTime(time: String) {
        dataStore.edit { preferences ->
            preferences[EXPIRY_MORNING_TIME] = time
        }
    }

    suspend fun setExpiryEveningTime(time: String) {
        dataStore.edit { preferences ->
            preferences[EXPIRY_EVENING_TIME] = time
        }
    }
}

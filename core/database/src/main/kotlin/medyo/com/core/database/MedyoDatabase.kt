package medyo.com.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import medyo.com.core.database.converter.InstantConverter
import medyo.com.core.database.converter.StringListConverter
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.entity.MedicationEntity

@Database(entities = [MedicationEntity::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class, StringListConverter::class)
internal abstract class MedyoDatabase: RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
}
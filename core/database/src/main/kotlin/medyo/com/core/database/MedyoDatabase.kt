package medyo.com.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import medyo.com.core.database.converter.InstantConverter
import medyo.com.core.database.converter.StringListConverter
import medyo.com.core.database.dao.MedicationDao
import medyo.com.core.database.entity.MedicationEntity

import medyo.com.core.database.entity.MedicationInfoEntity

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import medyo.com.core.database.entity.ScheduleEntity
import medyo.com.core.database.entity.DosageHistoryEntity
import medyo.com.core.database.dao.DosageAlertDao

@Database(
    entities = [MedicationEntity::class, MedicationInfoEntity::class, ScheduleEntity::class, DosageHistoryEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(InstantConverter::class, StringListConverter::class)
internal abstract class MedyoDatabase: RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun dosageAlertDao(): DosageAlertDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE medications ADD COLUMN stockRemoved INTEGER NOT NULL DEFAULT 0")
            }
        }
        
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `schedules` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `medicationId` INTEGER NOT NULL, 
                        `timeOfDay` TEXT NOT NULL, 
                        `frequency` TEXT NOT NULL, 
                        `startDate` INTEGER NOT NULL, 
                        `endDate` INTEGER, 
                        FOREIGN KEY(`medicationId`) REFERENCES `medications`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_schedules_medicationId` ON `schedules` (`medicationId`)")
                
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `dosage_history` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `medicationId` INTEGER NOT NULL, 
                        `scheduleId` INTEGER, 
                        `scheduledTimestamp` INTEGER NOT NULL, 
                        `actualTakenTimestamp` INTEGER, 
                        `status` TEXT NOT NULL, 
                        FOREIGN KEY(`medicationId`) REFERENCES `medications`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, 
                        FOREIGN KEY(`scheduleId`) REFERENCES `schedules`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_dosage_history_medicationId` ON `dosage_history` (`medicationId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_dosage_history_scheduleId` ON `dosage_history` (`scheduleId`)")
            }
        }
    }
}
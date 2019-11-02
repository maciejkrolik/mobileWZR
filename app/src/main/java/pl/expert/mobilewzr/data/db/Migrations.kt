package pl.expert.mobilewzr.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `SubjectNew` (`title` TEXT NOT NULL, `startDate` INTEGER NOT NULL, `startTime` TEXT NOT NULL, `endDate` INTEGER NOT NULL, `endTime` TEXT NOT NULL, `description` TEXT NOT NULL, `location` TEXT NOT NULL, `isMyGroup` INTEGER NOT NULL, `index` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        database.execSQL("INSERT INTO SubjectNew(title, startDate, startTime, endDate, endTime, description, location, isMyGroup) SELECT title, startDate, startTime, endDate, endTime, description, location, 1 FROM Subject")
        database.execSQL("DROP TABLE Subject")
        database.execSQL("ALTER TABLE SubjectNew RENAME TO Subject")
    }

}
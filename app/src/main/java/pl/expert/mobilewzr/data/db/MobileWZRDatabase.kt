package pl.expert.mobilewzr.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.expert.mobilewzr.data.converter.RoomConverters
import pl.expert.mobilewzr.data.model.Subject

@Database(entities = [Subject::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class MobileWZRDatabase : RoomDatabase() {

    abstract fun subjectsDao(): SubjectsDao
}
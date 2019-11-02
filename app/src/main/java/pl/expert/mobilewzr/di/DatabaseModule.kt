package pl.expert.mobilewzr.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.db.Migration1To2
import pl.expert.mobilewzr.data.db.MobileWZRDatabase
import pl.expert.mobilewzr.data.db.SubjectsDao
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val context: Context
) {

    private val MIGRATION_1_2 = Migration1To2()

    @Singleton
    @Provides
    fun provideSubjectsDao(database: MobileWZRDatabase): SubjectsDao {
        return database.subjectsDao()
    }

    @Singleton
    @Provides
    fun provideMobileWZRDatabase(): MobileWZRDatabase {
        return Room.databaseBuilder(
            this.context,
            MobileWZRDatabase::class.java, "mobile-wzr-database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

}
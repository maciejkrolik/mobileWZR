package pl.expert.mobilewzr.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.MobileWZRDatabase
import pl.expert.mobilewzr.data.SubjectsDao
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val context: Context
) {

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
        ).build()
    }
}
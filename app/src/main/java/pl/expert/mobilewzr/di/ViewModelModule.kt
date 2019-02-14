package pl.expert.mobilewzr.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.ViewModelFactory
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: SubjectsRepository): ViewModelProvider.Factory {
        return ViewModelFactory(repository)
    }
}
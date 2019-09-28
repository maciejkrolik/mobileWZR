package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.NewsRepository
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(
        subjectsRepository: SubjectsRepository,
        newsRepository: NewsRepository
    ): ViewModelProvider.Factory {
        return ViewModelFactory(subjectsRepository, newsRepository)
    }

}
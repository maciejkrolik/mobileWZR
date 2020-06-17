package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.*
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(
        subjectsRepository: SubjectsRepository,
        newsRepository: NewsRepository,
        groupsRepository: GroupsRepository,
        lecturersRepository: LecturersRepository,
        messagesRepository: MessagesRepository,
        firebaseAuth: FirebaseAuth
    ): ViewModelProvider.Factory {
        return ViewModelFactory(
            subjectsRepository,
            newsRepository,
            groupsRepository,
            lecturersRepository,
            messagesRepository,
            firebaseAuth
        )
    }

}
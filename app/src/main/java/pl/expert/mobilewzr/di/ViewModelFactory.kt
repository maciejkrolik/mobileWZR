package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import pl.expert.mobilewzr.data.*
import pl.expert.mobilewzr.ui.common.thread.ThreadViewModel
import pl.expert.mobilewzr.ui.lecturers.lecturerstimetable.LecturersTimetableViewModel
import pl.expert.mobilewzr.ui.lecturers.LecturersViewModel
import pl.expert.mobilewzr.ui.lecturers.lecturerlogin.LecturersLoginViewModel
import pl.expert.mobilewzr.ui.lecturers.lecturerregister.LecturersRegisterViewModel
import pl.expert.mobilewzr.ui.lecturers.lecturersmessages.LecturersMessagesViewModel
import pl.expert.mobilewzr.ui.news.NewsViewModel
import pl.expert.mobilewzr.ui.search.SearchViewModel
import pl.expert.mobilewzr.ui.timetable.TimetableViewModel
import pl.expert.mobilewzr.ui.timetable.editview.EditViewViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val subjectsRepository: SubjectsRepository,
    private val newsRepository: NewsRepository,
    private val groupsRepository: GroupsRepository,
    private val lecturersRepository: LecturersRepository,
    private val messagesRepository: MessagesRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(TimetableViewModel::class.java) ->
                    TimetableViewModel(subjectsRepository)
                isAssignableFrom(NewsViewModel::class.java) ->
                    NewsViewModel(newsRepository)
                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(groupsRepository)
                isAssignableFrom(EditViewViewModel::class.java) ->
                    EditViewViewModel(subjectsRepository)
                isAssignableFrom(LecturersTimetableViewModel::class.java) ->
                    LecturersTimetableViewModel(subjectsRepository)
                isAssignableFrom(LecturersViewModel::class.java) ->
                    LecturersViewModel(lecturersRepository)
                isAssignableFrom(LecturersLoginViewModel::class.java) ->
                    LecturersLoginViewModel(firebaseAuth)
                isAssignableFrom(LecturersMessagesViewModel::class.java) ->
                    LecturersMessagesViewModel(firebaseAuth, groupsRepository, messagesRepository)
                isAssignableFrom(ThreadViewModel::class.java) ->
                    ThreadViewModel(messagesRepository, firebaseAuth)
                isAssignableFrom(LecturersRegisterViewModel::class.java) ->
                    LecturersRegisterViewModel(lecturersRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
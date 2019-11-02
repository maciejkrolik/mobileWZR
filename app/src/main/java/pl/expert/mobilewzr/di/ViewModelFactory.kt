package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.expert.mobilewzr.data.GroupsRepository
import pl.expert.mobilewzr.data.LecturersRepository
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.ui.lecturers.lecturerstimetable.LecturersTimetableViewModel
import pl.expert.mobilewzr.ui.lecturers.LecturersViewModel
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
    private val lecturersRepository: LecturersRepository
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
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
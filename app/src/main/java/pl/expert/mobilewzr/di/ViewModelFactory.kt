package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.ui.timetable.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.news.NewsViewModel
import pl.expert.mobilewzr.ui.search.SearchViewModel
import pl.expert.mobilewzr.ui.timetable.editview.EditViewViewModel
import pl.expert.mobilewzr.ui.timetable.weekview.WeekViewViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val subjectsRepository: SubjectsRepository,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(WeekViewViewModel::class.java) ->
                    WeekViewViewModel(subjectsRepository)
                isAssignableFrom(NewsViewModel::class.java) ->
                    NewsViewModel(newsRepository)
                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(subjectsRepository)
                isAssignableFrom(DayViewViewModel::class.java) ->
                    DayViewViewModel(subjectsRepository)
                isAssignableFrom(EditViewViewModel::class.java) ->
                    EditViewViewModel(subjectsRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
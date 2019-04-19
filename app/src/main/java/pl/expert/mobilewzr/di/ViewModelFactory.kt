package pl.expert.mobilewzr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.ui.timetableviews.dayview.DayViewViewModel
import pl.expert.mobilewzr.ui.newsview.NewsViewViewModel
import pl.expert.mobilewzr.ui.searchview.SearchViewViewModel
import pl.expert.mobilewzr.ui.timetableviews.editview.EditViewViewModel
import pl.expert.mobilewzr.ui.timetableviews.weekview.WeekViewViewModel
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
                isAssignableFrom(NewsViewViewModel::class.java) ->
                    NewsViewViewModel(newsRepository)
                isAssignableFrom(SearchViewViewModel::class.java) ->
                    SearchViewViewModel(subjectsRepository)
                isAssignableFrom(DayViewViewModel::class.java) ->
                    DayViewViewModel(subjectsRepository)
                isAssignableFrom(EditViewViewModel::class.java) ->
                    EditViewViewModel(subjectsRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
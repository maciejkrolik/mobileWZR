package pl.expert.mobilewzr

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.ui.weekview.WeekViewViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val repository: SubjectsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(WeekViewViewModel::class.java) ->
                    WeekViewViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
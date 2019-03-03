package pl.expert.mobilewzr.ui.weekview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.WeekViewItem

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private var listOfWeekViewItems = MutableLiveData<List<WeekViewItem>>()
    private var previouslySelectedGroupId = ""

    fun loadSubjectsFromRepository(groupId: String) {
        if (listOfWeekViewItems.value.isNullOrEmpty() || groupId != previouslySelectedGroupId) {
            listOfWeekViewItems = repository.getListOfWeekViewItems(groupId)
            previouslySelectedGroupId = groupId
        }
    }

    fun getSubjects(weekNumber: Int): LiveData<List<WeekViewItem>> {
        return Transformations.map(listOfWeekViewItems) { listOfWeekViewItems ->
            when (weekNumber) {
                0 -> listOfWeekViewItems.take(15)
                1 -> listOfWeekViewItems.takeLast(15)
                else -> {
                    throw IllegalArgumentException("Unknown week number: $weekNumber")
                }
            }
        } as LiveData<List<WeekViewItem>>
    }
}
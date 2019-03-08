package pl.expert.mobilewzr.ui.weekview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.SubjectsWithWeekViews
import pl.expert.mobilewzr.data.dto.WeekViewItem

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private var subjectsWithWeekViews = MutableLiveData<SubjectsWithWeekViews>()
    private var previouslySelectedGroupId = ""

    fun loadSubjectsFromRepository(groupId: String) {
        if (subjectsWithWeekViews.value?.listOfSubjects.isNullOrEmpty() || groupId != previouslySelectedGroupId) {
            subjectsWithWeekViews = repository.getSubjectsWithWeekViews(groupId)
            previouslySelectedGroupId = groupId
        }
    }

    fun getSubjects(): LiveData<List<Subject>> {
        return Transformations.map(subjectsWithWeekViews) { subjectsWithWeekViews ->
            subjectsWithWeekViews.listOfSubjects
        } as LiveData<List<Subject>>
    }

    fun getWeekViewItems(weekNumber: Int): LiveData<List<WeekViewItem>> {
        return Transformations.map(subjectsWithWeekViews) { subjectsWithWeekViews ->
            when (weekNumber) {
                0 -> subjectsWithWeekViews.listOfWeekViewItems.take(15)
                1 -> subjectsWithWeekViews.listOfWeekViewItems.takeLast(15)
                else -> {
                    throw IllegalArgumentException("Unknown week number: $weekNumber")
                }
            }
        } as LiveData<List<WeekViewItem>>
    }
}
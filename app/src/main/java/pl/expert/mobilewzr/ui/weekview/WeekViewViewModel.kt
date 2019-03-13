package pl.expert.mobilewzr.ui.weekview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.SubjectsWithWeekViews
import pl.expert.mobilewzr.data.dto.WeekViewItem

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private lateinit var subjectsWithWeekViews: MutableLiveData<SubjectsWithWeekViews>

    var groupId = ""

    fun loadSubjectsFromRepository(groupId: String) {
        if (!::subjectsWithWeekViews.isInitialized || groupId != this.groupId) {
            subjectsWithWeekViews = repository.getSubjectsWithWeekViews(groupId)
            this.groupId = groupId
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

    fun replaceTimetableInLocalDb() {
    }
}
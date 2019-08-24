package pl.expert.mobilewzr.ui.timetable.dayview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.DayViewItem
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class DayViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private lateinit var dayViewDataHolder: MutableLiveData<DayViewDataHolder>
    private lateinit var timetableViewLocation: TimetableViewLocation

    private var idOfAGroupSavedInDb = ""
    var groupId = ""

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    fun checkIfSubjectsLoaded(groupId: String, timetableViewLocation: TimetableViewLocation) {
        if (!::dayViewDataHolder.isInitialized || groupId != this.groupId || timetableViewLocation != this.timetableViewLocation) {
            this.timetableViewLocation = timetableViewLocation
            loadSubjects(groupId)
            this.groupId = groupId
        }
    }

    private fun loadSubjects(groupId: String) {
        if (!isTimetableSavedInDb(groupId) || timetableViewLocation == TimetableViewLocation.SEARCH) dayViewDataHolder =
            repository.getDayViewDataFromService(groupId)
        else {
            dayViewDataHolder = MutableLiveData()
            viewModelScope.launch {
                dayViewDataHolder = repository.getDayViewDataFromDb(dayViewDataHolder)
            }
        }
    }

    private fun isTimetableSavedInDb(groupId: String): Boolean {
        return groupId == idOfAGroupSavedInDb
    }

    fun reloadSubjects() {
        loadSubjects(groupId)
    }

    fun getDayViewItems(weekNumber: Int, weekDay: Int): LiveData<List<DayViewItem>> {
        return Transformations.map(dayViewDataHolder) { dayViewDataHolder ->
            when (weekNumber) {
                0 -> {
                    when (weekDay) {
                        0 -> dayViewDataHolder.AWeek.mondaySubjects
                        1 -> dayViewDataHolder.AWeek.tuesdaySubjects
                        2 -> dayViewDataHolder.AWeek.wednesdaySubjects
                        3 -> dayViewDataHolder.AWeek.thursdaySubjects
                        4 -> dayViewDataHolder.AWeek.fridaySubjects
                        else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                    }
                }
                1 -> {
                    when (weekDay) {
                        0 -> dayViewDataHolder.BWeek.mondaySubjects
                        1 -> dayViewDataHolder.BWeek.tuesdaySubjects
                        2 -> dayViewDataHolder.BWeek.wednesdaySubjects
                        3 -> dayViewDataHolder.BWeek.thursdaySubjects
                        4 -> dayViewDataHolder.BWeek.fridaySubjects
                        else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                    }
                }
                else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
            }
        }
    }

    fun getDayViewDataHolder(): LiveData<DayViewDataHolder> {
        return dayViewDataHolder
    }

    fun replaceSubjectsInDb() {
        viewModelScope.launch {
            repository.replaceSubjectsInDb(dayViewDataHolder.value?.subjects!!)
        }
    }

    fun setIdOfAGroupSavedInDb(groupId: String) {
        idOfAGroupSavedInDb = groupId
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
package pl.expert.mobilewzr.ui.timetable.dayview

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class DayViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private lateinit var dayViewDataHolder: MutableLiveData<DayViewDataHolder>
    private lateinit var timetableViewLocation: TimetableViewLocation

    private var idOfAGroupSavedInDb = ""
    var groupId = ""
    val daySubjects = MutableLiveData<List<SubjectItem>>()

    fun checkIfSubjectsLoaded(groupId: String, timetableViewLocation: TimetableViewLocation) {
        if (!::dayViewDataHolder.isInitialized || groupId != this.groupId || timetableViewLocation != this.timetableViewLocation) {
            this.timetableViewLocation = timetableViewLocation
            loadSubjects(groupId)
            this.groupId = groupId
        }
    }

    private fun loadSubjects(groupId: String) {
        if (!isTimetableSavedInDb(groupId) || timetableViewLocation == TimetableViewLocation.SEARCH)
            dayViewDataHolder = repository.getDayViewDataFromService(groupId)
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

    fun getCalendarDayViewItems(weekNumber: Int, weekDay: Int) {
        daySubjects.value = when (weekNumber) {
            0 -> {
                when (weekDay) {
                    0 -> dayViewDataHolder.value!!.weekA.mondaySubjects
                    1 -> dayViewDataHolder.value!!.weekA.tuesdaySubjects
                    2 -> dayViewDataHolder.value!!.weekA.wednesdaySubjects
                    3 -> dayViewDataHolder.value!!.weekA.thursdaySubjects
                    4 -> dayViewDataHolder.value!!.weekA.fridaySubjects
                    5 -> dayViewDataHolder.value!!.weekA.saturdaySubjects
                    6 -> dayViewDataHolder.value!!.weekA.sundaySubjects
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                }
            }
            1 -> {
                when (weekDay) {
                    0 -> dayViewDataHolder.value!!.weekB.mondaySubjects
                    1 -> dayViewDataHolder.value!!.weekB.tuesdaySubjects
                    2 -> dayViewDataHolder.value!!.weekB.wednesdaySubjects
                    3 -> dayViewDataHolder.value!!.weekB.thursdaySubjects
                    4 -> dayViewDataHolder.value!!.weekB.fridaySubjects
                    5 -> dayViewDataHolder.value!!.weekB.saturdaySubjects
                    6 -> dayViewDataHolder.value!!.weekB.sundaySubjects
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                }
            }
            else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
        }
    }

    fun getDayViewItems(weekNumber: Int, weekDay: Int): LiveData<List<SubjectItem>> {
        return Transformations.map(dayViewDataHolder) { dayViewDataHolder ->
            when (weekNumber) {
                0 -> {
                    when (weekDay) {
                        0 -> dayViewDataHolder.weekA.mondaySubjects
                        1 -> dayViewDataHolder.weekA.tuesdaySubjects
                        2 -> dayViewDataHolder.weekA.wednesdaySubjects
                        3 -> dayViewDataHolder.weekA.thursdaySubjects
                        4 -> dayViewDataHolder.weekA.fridaySubjects
                        5 -> dayViewDataHolder.weekA.saturdaySubjects
                        6 -> dayViewDataHolder.weekA.sundaySubjects
                        else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                    }
                }
                1 -> {
                    when (weekDay) {
                        0 -> dayViewDataHolder.weekB.mondaySubjects
                        1 -> dayViewDataHolder.weekB.tuesdaySubjects
                        2 -> dayViewDataHolder.weekB.wednesdaySubjects
                        3 -> dayViewDataHolder.weekB.thursdaySubjects
                        4 -> dayViewDataHolder.weekB.fridaySubjects
                        5 -> dayViewDataHolder.weekB.saturdaySubjects
                        6 -> dayViewDataHolder.weekB.sundaySubjects
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
            repository.replaceSubjectsInDb(dayViewDataHolder.value?.allSubjects!!)
        }
    }

    fun setIdOfAGroupSavedInDb(groupId: String) {
        idOfAGroupSavedInDb = groupId
    }

}
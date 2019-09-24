package pl.expert.mobilewzr.ui.timetable.weekview

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.domain.domainmodel.WeekViewDataHolder
import pl.expert.mobilewzr.ui.timetable.TimetableViewLocation

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private lateinit var weekViewDataHolder: MutableLiveData<WeekViewDataHolder>
    lateinit var timetableViewLocation: TimetableViewLocation

    private var idOfAGroupSavedInDb = ""
    var groupId = ""

    fun checkIfSubjectsLoaded(groupId: String, timetableViewLocation: TimetableViewLocation) {
        if (!::weekViewDataHolder.isInitialized || groupId != this.groupId || timetableViewLocation != this.timetableViewLocation) {
            this.timetableViewLocation = timetableViewLocation
            loadSubjects(groupId)
            this.groupId = groupId
        }
    }

    private fun loadSubjects(groupId: String) {
        if (!isTimetableSavedInDb(groupId) || timetableViewLocation == TimetableViewLocation.SEARCH) {
            weekViewDataHolder = repository.getWeekViewDataFromService(groupId)
        } else {
            weekViewDataHolder = MutableLiveData()
            viewModelScope.launch {
                weekViewDataHolder = repository.getWeekViewDataFromDb(weekViewDataHolder)
            }
        }
    }

    private fun isTimetableSavedInDb(groupId: String): Boolean {
        return groupId == idOfAGroupSavedInDb
    }

    fun reloadSubjects() {
        loadSubjects(groupId)
    }

    fun getSpecificWeekSubjects(weekNumber: Int): LiveData<List<Subject>> {
        return Transformations.map(weekViewDataHolder) { weekViewDataHolder ->
            when (weekNumber) {
                0 -> weekViewDataHolder.weekA
                1 -> weekViewDataHolder.weekB
                else -> {
                    throw IllegalArgumentException("Unknown week number: $weekNumber")
                }
            }
        }
    }

    fun replaceSubjectsInDb() {
        viewModelScope.launch {
            repository.replaceSubjectsInDb(weekViewDataHolder.value?.allSubjects!!)
        }
    }

    fun setIdOfAGroupSavedInDb(groupId: String) {
        idOfAGroupSavedInDb = groupId
    }

}
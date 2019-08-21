package pl.expert.mobilewzr.ui.timetableviews.weekview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.dto.WeekViewDataHolder
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.ui.timetableviews.TimetableViewLocation

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private lateinit var weekViewDataHolder: MutableLiveData<WeekViewDataHolder>
    private lateinit var timetableViewLocation: TimetableViewLocation

    private var idOfAGroupSavedInDb = ""
    var groupId = ""

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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

    fun getSubjects(): LiveData<List<Subject>> {
        return Transformations.map(weekViewDataHolder) { weekViewDataHolder ->
            weekViewDataHolder.subjects
        }
    }

    fun getSpecificWeekSubjects(weekNumber: Int): LiveData<List<Subject>> {
        return Transformations.map(weekViewDataHolder) { weekViewDataHolder ->
            when (weekNumber) {
                0 -> weekViewDataHolder.AWeek
                1 -> weekViewDataHolder.BWeek
                else -> {
                    throw IllegalArgumentException("Unknown week number: $weekNumber")
                }
            }
        }
    }

    fun replaceSubjectsInDb() {
        viewModelScope.launch {
            repository.replaceSubjectsInDb(weekViewDataHolder.value?.subjects!!)
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
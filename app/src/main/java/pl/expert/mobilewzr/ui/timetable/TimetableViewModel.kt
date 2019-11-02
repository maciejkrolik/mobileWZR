package pl.expert.mobilewzr.ui.timetable

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.domain.domainmodel.TimetableDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.util.CalendarUtils
import pl.expert.mobilewzr.util.ResourceState
import java.util.*
import javax.inject.Inject

class TimetableViewModel @Inject constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    val timetableDataHolder = MutableLiveData<ResourceState<TimetableDataHolder>>(ResourceState.Loading())

    lateinit var timetableViewLocation: TimetableViewLocation
    private var idOfAGroupSavedInDb = ""
    var groupId = ""

    fun init(groupId: String, timetableViewLocation: TimetableViewLocation) {
        idOfAGroupSavedInDb = groupId

        if (timetableDataHolder.value?.data?.allSubjects.isNullOrEmpty()
            || groupId != this.groupId
            || timetableViewLocation != this.timetableViewLocation
        ) {
            this.timetableViewLocation = timetableViewLocation
            this.groupId = groupId
            loadSubjects(groupId)
        }
    }

    private fun loadSubjects(groupId: String) {
        timetableDataHolder.value = ResourceState.Loading()
        viewModelScope.launch {
            if (!isTimetableSavedInDb(groupId) || timetableViewLocation == TimetableViewLocation.SEARCH) {
                timetableDataHolder.postValue(
                    if (groupId.startsWith("S")) {
                        ResourceState.Success(repository.getFirstTwoWeeksTimetableDataFromService(groupId))
                    } else {
                        ResourceState.Success(repository.getFullTimetableDataFromService(groupId))
                    }
                )
            } else {
                val timetableData = ResourceState.Success(repository.getTimetableDataFromDb())
                timetableDataHolder.postValue(timetableData)
            }
        }
    }

    fun getWeekSpecificSubjects(weekNumber: Int): List<Subject> {
        return when (weekNumber) {
            0 -> {
                timetableDataHolder.value?.data?.allSubjects!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 0 }
            }
            1 -> {
                timetableDataHolder.value?.data?.allSubjects!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 1 }
            }
            else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
        }
    }

    fun getCalendarDayViewItems(weekNumber: Int, weekDay: Int, date: Date): List<SubjectItem> {
        return if (groupId.startsWith("S")) {
            getDaySpecificSubjects(weekNumber, weekDay)
        } else {
            timetableDataHolder.value?.data?.allSubjects!!
                .filter { s -> s.startDate == date }
                .map { s -> SubjectItem(s) }
        }
    }

    fun getDayViewItems(weekNumber: Int, weekDay: Int): List<SubjectItem> {
        return getDaySpecificSubjects(weekNumber, weekDay)
    }

    private fun getDaySpecificSubjects(weekNumber: Int, weekDay: Int): List<SubjectItem> {
        return when (weekNumber) {
            0 -> {
                when (weekDay) {
                    0 -> timetableDataHolder.value?.data?.weekA?.mondaySubjects!!
                    1 -> timetableDataHolder.value?.data?.weekA?.tuesdaySubjects!!
                    2 -> timetableDataHolder.value?.data?.weekA?.wednesdaySubjects!!
                    3 -> timetableDataHolder.value?.data?.weekA?.thursdaySubjects!!
                    4 -> timetableDataHolder.value?.data?.weekA?.fridaySubjects!!
                    5 -> timetableDataHolder.value?.data?.weekA?.saturdaySubjects!!
                    6 -> timetableDataHolder.value?.data?.weekA?.sundaySubjects!!
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                }
            }
            1 -> {
                when (weekDay) {
                    0 -> timetableDataHolder.value?.data?.weekB?.mondaySubjects!!
                    1 -> timetableDataHolder.value?.data?.weekB?.tuesdaySubjects!!
                    2 -> timetableDataHolder.value?.data?.weekB?.wednesdaySubjects!!
                    3 -> timetableDataHolder.value?.data?.weekB?.thursdaySubjects!!
                    4 -> timetableDataHolder.value?.data?.weekB?.fridaySubjects!!
                    5 -> timetableDataHolder.value?.data?.weekB?.saturdaySubjects!!
                    6 -> timetableDataHolder.value?.data?.weekB?.sundaySubjects!!
                    else -> throw java.lang.IllegalArgumentException("Unknown week day number: $weekDay")
                }
            }
            else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
        }
    }

    fun reloadSubjects() {
        loadSubjects(groupId)
    }

    fun replaceSubjectsInDb() {
        viewModelScope.launch {
            repository.replaceMyGroupSubjectsInDbWith(timetableDataHolder.value?.data?.allSubjects!!)
        }
    }

    private fun isTimetableSavedInDb(groupId: String): Boolean {
        return groupId == idOfAGroupSavedInDb
    }

}
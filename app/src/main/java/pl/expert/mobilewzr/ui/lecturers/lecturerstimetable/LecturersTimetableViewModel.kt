package pl.expert.mobilewzr.ui.lecturers.lecturerstimetable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.util.CalendarUtils
import pl.expert.mobilewzr.util.ResourceState

class LecturersTimetableViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    val subjects = MutableLiveData<ResourceState<List<Subject>>>(ResourceState.Loading())

    fun getLecturersSubjectsFromDb(lecturerName: String) {
        viewModelScope.launch {
            try {
                val lecturerSubjects = repository.getSubjectsByLecturerFromDb(lecturerName)
                subjects.postValue(ResourceState.Success(lecturerSubjects))
            } catch (ex: Exception) {
                subjects.postValue(ResourceState.Error("Error while getting lecturer subjects from db"))
            }
        }
    }

    fun getWeekSpecificSubjects(weekNumber: Int): List<Subject> {
        return when (weekNumber) {
            0 -> subjects.value?.data!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 0 }
            1 -> subjects.value?.data!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 1 }
            else -> throw Exception("Unknown week number: $weekNumber")
        }
    }

    fun getLecturersSubjects() {
        viewModelScope.launch {
            repository.getAllFirstTwoWeeksSubjectsFromService()
        }
    }

}
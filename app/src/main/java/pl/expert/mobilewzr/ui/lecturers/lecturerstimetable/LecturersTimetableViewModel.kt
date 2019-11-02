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

    val subjectsState = MutableLiveData<ResourceState<List<Subject>>>(ResourceState.Loading())

    fun getLecturersSubjectsFromDb(lecturerName: String) {
        subjectsState.value = ResourceState.Loading()
        viewModelScope.launch {
            if (areLecturersSubjectsDownloaded()) {
                try {
                    val lecturerSubjects = repository.getSubjectsByLecturerFromDb(lecturerName)
                    subjectsState.postValue(ResourceState.Success(lecturerSubjects))
                } catch (ex: Exception) {
                    subjectsState.postValue(ResourceState.Error("Error while getting lecturer subjectsState from db"))
                }
            } else {
                subjectsState.postValue(ResourceState.Error("No lecturers subjects in db"))
            }
        }
    }

    fun getWeekSpecificSubjects(weekNumber: Int): List<Subject> {
        return when (weekNumber) {
            0 -> subjectsState.value?.data!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 0 }
            1 -> subjectsState.value?.data!!.filter { s -> CalendarUtils.getWeekNumber(s.startDate) == 1 }
            else -> throw IllegalArgumentException("Unknown week number: $weekNumber")
        }
    }

    fun getLecturersSubjects(lecturerName: String) {
        subjectsState.value = ResourceState.Loading()
        viewModelScope.launch {
            repository.getAllFirstTwoWeeksSubjectsFromService()
            getLecturersSubjectsFromDb(lecturerName)
        }
    }

    private suspend fun areLecturersSubjectsDownloaded() = repository.areLecturersSubjectsDownloaded()

}
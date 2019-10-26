package pl.expert.mobilewzr.ui.lecturers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.util.CalendarUtils

class LecturersTimetableViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    val subjects = MutableLiveData<List<Subject>>()

    init {
        getLecturersSubjectsFromDb()
    }

    private fun getLecturersSubjectsFromDb() {
        viewModelScope.launch {
            subjects.postValue(repository.getSubjectsByLecturerFromDb("dr hab. Michał Kuciapski").filter { s ->
                CalendarUtils.getWeekNumber(
                    s.startDate
                ) == 0
            })
        }
    }

    fun getLecturersSubjects() {
        viewModelScope.launch {
            repository.getAllFirstTwoWeeksSubjectsFromService()
        }
    }

}
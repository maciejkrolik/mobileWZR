package pl.expert.mobilewzr.ui.timetableviews.editview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.Subject

class EditViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    var groupId = ""

    private val _isUpdatingDb = MutableLiveData<Boolean>()
    val isUpdatingDb: LiveData<Boolean>
        get() = _isUpdatingDb

    private var subjects = MutableLiveData<List<Subject>>()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getSubjects(groupId: String): LiveData<List<Subject>> {
        if (subjects.value.isNullOrEmpty() || groupId != this.groupId) {
            viewModelScope.launch {
                subjects.value = repository.getSubjectsFromDb()
            }
            this.groupId = groupId
        }
        return subjects
    }

    fun updateSubject(subjectIndex: Int, newSubject: Subject) {
        _isUpdatingDb.value = true
        viewModelScope.launch {
            val subject = subjects.value?.get(subjectIndex)!!
            subject.title = newSubject.title
            subject.description = newSubject.description
            subject.startTime = newSubject.startTime
            subject.startDate = newSubject.startDate
            repository.updateSubject(subject)
            _isUpdatingDb.postValue(false)
        }
    }

    fun addSubject(newSubject: Subject) {
        _isUpdatingDb.value = true
        viewModelScope.launch {
            val subject = Subject()
            subject.title = newSubject.title
            subject.description = newSubject.description
            subject.startTime = newSubject.startTime
            subject.startDate = newSubject.startDate
            repository.addSubject(subject)
            _isUpdatingDb.postValue(false)
        }
    }

    fun deleteSubject(subjectIndex: Int) {
        _isUpdatingDb.value = true
        viewModelScope.launch {
            val subject = subjects.value?.get(subjectIndex)!!
            repository.deleteSubject(subject)
            _isUpdatingDb.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
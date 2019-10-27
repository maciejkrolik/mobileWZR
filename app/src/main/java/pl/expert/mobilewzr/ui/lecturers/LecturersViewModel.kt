package pl.expert.mobilewzr.ui.lecturers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.LecturersRepository
import pl.expert.mobilewzr.data.model.Lecturer
import pl.expert.mobilewzr.util.ResourceState

class LecturersViewModel constructor(
    private val repository: LecturersRepository
) : ViewModel() {

    val lecturersState = MutableLiveData<ResourceState<List<Lecturer>>>(ResourceState.Loading())

    init {
        getLecturers()
    }

    fun getLecturers(): LiveData<ResourceState<List<Lecturer>>> {
        lecturersState.value = ResourceState.Loading()
        viewModelScope.launch {
            try {
                val lecturers = repository.getLecturers()
                lecturersState.value = ResourceState.Success(lecturers)
            } catch (ex: Exception) {
                lecturersState.value = ResourceState.Error(ex.message ?: "")
            }
        }
        return lecturersState
    }

}
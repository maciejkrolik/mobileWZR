package pl.expert.mobilewzr.ui.lecturers.lecturerregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.LecturersRepository
import pl.expert.mobilewzr.util.ResourceState
import javax.inject.Inject

class LecturersRegisterViewModel @Inject constructor(
    private val lecturersRepository: LecturersRepository
) : ViewModel() {

    val registerState = MutableLiveData<ResourceState<Unit>>(ResourceState.Idle())

    fun register(email: String, password: String, token: String) {
        registerState.value = ResourceState.Loading()
        viewModelScope.launch {
            try {
                lecturersRepository.registerLecturer(email, password, token)
                registerState.postValue(ResourceState.Success(Unit))
            } catch (ex: Exception) {
                registerState.postValue(ResourceState.Error(ex.message.toString()))
            }
        }
    }

}
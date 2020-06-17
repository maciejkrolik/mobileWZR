package pl.expert.mobilewzr.ui.lecturers.lecturerlogin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pl.expert.mobilewzr.util.ResourceState
import javax.inject.Inject

class LecturersLoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val loginState = MutableLiveData<ResourceState<AuthResult>>(ResourceState.Idle())

    fun login(email: String, password: String) {
        loginState.value = ResourceState.Loading()
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                loginState.postValue(ResourceState.Success(authResult))
            } catch (ex: Exception) {
                loginState.postValue(ResourceState.Error(ex.message.toString()))
            }
        }
    }

}
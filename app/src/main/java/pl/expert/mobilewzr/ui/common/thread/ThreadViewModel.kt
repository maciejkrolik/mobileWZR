package pl.expert.mobilewzr.ui.common.thread

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.MessagesRepository
import pl.expert.mobilewzr.data.model.Message
import pl.expert.mobilewzr.util.ResourceState
import java.util.*
import javax.inject.Inject

class ThreadViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val getMessagesState = MutableLiveData<ResourceState<List<Message>>>(ResourceState.Loading())
    val sendMessageState = MutableLiveData<ResourceState<DocumentReference>>(ResourceState.Idle())

    fun getThreadMessages(studentGroup: String) {
        firebaseAuth.currentUser?.displayName
        getMessagesState.value = ResourceState.Loading()
        firebaseAuth.uid?.let { lecturerUID ->
            viewModelScope.launch {
                try {
                    val messages = messagesRepository.getThreadMessages(lecturerUID, studentGroup)
                    getMessagesState.postValue(ResourceState.Success(messages.sortedBy { it.date }))
                } catch (ex: Exception) {
                    getMessagesState.postValue(ResourceState.Error(ex.message.toString()))
                }
            }
        }
    }

    fun sendMessage(messageContent: String, studentGroup: String) {
        sendMessageState.value = ResourceState.Loading()
        firebaseAuth.uid?.let { lecturerUID ->
            val message = Message(messageContent, true, lecturerUID, studentGroup, Date())
            viewModelScope.launch {
                try {
                    sendMessageState.postValue(ResourceState.Success(messagesRepository.sendMessage(message)))
                } catch (ex: Exception) {
                    sendMessageState.postValue(ResourceState.Error(ex.message.toString()))
                }
            }
        }
    }

}
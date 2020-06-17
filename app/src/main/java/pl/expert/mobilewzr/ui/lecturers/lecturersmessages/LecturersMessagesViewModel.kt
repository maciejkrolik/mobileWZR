package pl.expert.mobilewzr.ui.lecturers.lecturersmessages

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.GroupsRepository
import pl.expert.mobilewzr.data.MessagesRepository
import pl.expert.mobilewzr.domain.domainmodel.ThreadItem
import pl.expert.mobilewzr.util.ResourceState
import javax.inject.Inject

private const val TAG = "LecturersMessages"

class LecturersMessagesViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val groupsRepository: GroupsRepository,
    private val messagesRepository: MessagesRepository
) : ViewModel() {

    val threadsState = MutableLiveData<ResourceState<List<ThreadItem>>>(ResourceState.Loading())
    val groupsState = MutableLiveData<ResourceState<List<String>>>(ResourceState.Idle())

    init {
        getMessages()
    }

    fun getMessages() {
        threadsState.value = ResourceState.Loading()
        viewModelScope.launch {
            firebaseAuth.uid?.let { lecturerUID ->
                try {
                    val threads = messagesRepository.getLecturersMessages(lecturerUID)
                        .sortedBy { it.date }
                        .groupBy { it.studentGroup }
                        .map {
                            ThreadItem(
                                it.key,
                                it.value.lastOrNull()?.content,
                                it.value.lastOrNull()?.date,
                                it.value.lastOrNull()?.documentId
                            )
                        }
                        .sortedByDescending { it.lastMessageDate }
                    threadsState.postValue(ResourceState.Success(threads))
                } catch (ex: Exception) {
                    threadsState.postValue(ResourceState.Error(ex.message.toString()))
                }
            }
        }
    }

    fun loadGroupPicker() {
        groupsState.value = ResourceState.Loading()
        viewModelScope.launch {
            try {
                groupsState.postValue(ResourceState.Success(groupsRepository.getGroups()))
            } catch (ex: Exception) {
                Log.e(TAG, ex.message.toString())
            }
        }
    }

}
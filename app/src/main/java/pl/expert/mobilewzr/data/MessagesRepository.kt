package pl.expert.mobilewzr.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.expert.mobilewzr.data.model.Message
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getThreadMessages(lecturerUID: String, groupId: String): List<Message> {
        return firestore.collection("messages")
            .whereEqualTo("studentGroup", groupId)
            .whereEqualTo("lecturerUID", lecturerUID)
            .get()
            .await()
            .toObjects(Message::class.java)
    }

    suspend fun getLecturersMessages(lecturerUID: String): List<Message> {
        return firestore.collection("messages")
            .whereEqualTo("lecturerUID", lecturerUID)
            .get()
            .await()
            .toObjects(Message::class.java)
    }

    suspend fun sendMessage(message: Message): DocumentReference {
        return firestore.collection("messages")
            .add(message)
            .await()
    }

}
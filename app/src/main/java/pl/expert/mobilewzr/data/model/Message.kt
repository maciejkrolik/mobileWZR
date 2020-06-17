package pl.expert.mobilewzr.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Message(

    val content: String? = null,

    @get:PropertyName("isLecturerMessage")
    val isLecturerMessage: Boolean? = null,

    val lecturerUID: String? = null,

    val studentGroup: String? = null,

    val date: Date? = null,

    @DocumentId
    val documentId: String? = null

)

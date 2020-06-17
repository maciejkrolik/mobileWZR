package pl.expert.mobilewzr.domain.domainmodel

import java.util.*

data class ThreadItem(

    val groupId: String? = null,

    val lastMessageContent: String? = null,

    val lastMessageDate: Date? = null,

    val lastMessageDocId: String? = null

)
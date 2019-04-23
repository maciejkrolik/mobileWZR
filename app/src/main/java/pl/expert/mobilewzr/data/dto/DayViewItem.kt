package pl.expert.mobilewzr.data.dto

import pl.expert.mobilewzr.data.model.Subject

data class DayViewItem(
    val index: Int,
    val startTime: String,
    val endTime: String,
    val title: String,
    val locationWithDescription: String
) {
    constructor(subject: Subject) : this(
        subject.index,
        subject.startTime,
        subject.endTime,
        subject.title,
        subject.location.substringBefore(',') + ", " + subject.description
    )
}
package pl.expert.mobilewzr.data.dto

import pl.expert.mobilewzr.data.model.Subject

data class DayViewItem(
    val startTime: String,
    val title: String
) {
    constructor(subject: Subject) : this(subject.startTime, subject.title)
}
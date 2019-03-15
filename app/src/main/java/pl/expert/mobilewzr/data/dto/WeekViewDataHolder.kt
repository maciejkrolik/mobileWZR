package pl.expert.mobilewzr.data.dto

import pl.expert.mobilewzr.data.model.Subject

data class WeekViewDataHolder(
    val subjects: List<Subject>,
    val weekViewItems: List<WeekViewItem>
)
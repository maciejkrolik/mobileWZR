package pl.expert.mobilewzr.data.dto

data class WeekViewItem(
    val time: String,
    val weekViewSubjectItems: MutableList<WeekViewSubjectItem>
)
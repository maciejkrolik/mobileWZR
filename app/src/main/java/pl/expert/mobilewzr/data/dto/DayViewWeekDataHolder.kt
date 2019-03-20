package pl.expert.mobilewzr.data.dto

data class DayViewWeekDataHolder(
    val mondaySubjects: MutableList<DayViewItem> = mutableListOf(),
    val tuesdaySubjects: MutableList<DayViewItem> = mutableListOf(),
    val wednesdaySubjects: MutableList<DayViewItem> = mutableListOf(),
    val thursdaySubjects: MutableList<DayViewItem> = mutableListOf(),
    val fridaySubjects: MutableList<DayViewItem> = mutableListOf()
)

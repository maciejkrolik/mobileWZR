package pl.expert.mobilewzr.data.dto

import pl.expert.mobilewzr.data.model.Subject

data class SubjectsWithWeekViews(
    val listOfSubjects: List<Subject>,
    val listOfWeekViewItems: List<WeekViewItem>
)
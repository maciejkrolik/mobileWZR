package pl.expert.mobilewzr.data.dto

import pl.expert.mobilewzr.data.model.Subject

data class DayViewDataHolder(
    val AWeek: DayViewWeekDataHolder,
    val BWeek: DayViewWeekDataHolder,
    val subjects: List<Subject>
)

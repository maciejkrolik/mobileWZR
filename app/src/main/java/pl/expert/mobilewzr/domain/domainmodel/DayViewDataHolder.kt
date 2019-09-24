package pl.expert.mobilewzr.domain.domainmodel

import pl.expert.mobilewzr.data.model.Subject

data class DayViewDataHolder(

    val weekA: DayViewWeekDataHolder,

    val weekB: DayViewWeekDataHolder,

    val allSubjects: List<Subject>

)

package pl.expert.mobilewzr.domain.domainmodel

import pl.expert.mobilewzr.data.model.Subject

data class TimetableDataHolder(

    val weekA: WeekDataHolder,

    val weekB: WeekDataHolder,

    val allSubjects: List<Subject>

)

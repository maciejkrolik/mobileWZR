package pl.expert.mobilewzr.domain.domainmodel

import pl.expert.mobilewzr.data.model.Subject

data class WeekViewDataHolder(

    val weekA: List<Subject>,

    val weekB: List<Subject>,

    val allSubjects: List<Subject>

)
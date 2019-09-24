package pl.expert.mobilewzr.domain.domainmodel

data class DayViewWeekDataHolder(

    val mondaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val tuesdaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val wednesdaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val thursdaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val fridaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val saturdaySubjects: MutableList<SubjectItem> = mutableListOf(),

    val sundaySubjects: MutableList<SubjectItem> = mutableListOf()

)

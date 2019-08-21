package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.dto.WeekViewDataHolder
import pl.expert.mobilewzr.data.model.Subject

abstract class WeekViewUtils {

    companion object {

        private lateinit var subjects: List<Subject>
        private val weekASubjects = mutableListOf<Subject>()
        private val weekBSubjects = mutableListOf<Subject>()

        fun getWeekViewDataHolder(subjects: List<Subject>): WeekViewDataHolder {
            this.subjects = subjects

            assignSubjectsToSpecificWeeks()

            return WeekViewDataHolder(weekASubjects, weekBSubjects, subjects)
        }

        private fun assignSubjectsToSpecificWeeks() {
            weekASubjects.clear()
            weekBSubjects.clear()

            subjects.forEach { subject ->
                when (CalendarUtils.getWeekNumber(subject.startDate)) {
                    0 -> weekASubjects.add(subject)
                    1 -> weekBSubjects.add(subject)
                }
            }
        }

    }
}
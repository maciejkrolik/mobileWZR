package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.domain.domainmodel.DayViewWeekDataHolder
import pl.expert.mobilewzr.data.model.Subject

abstract class DayViewUtils {

    companion object {

        private lateinit var AWeekDataHolder: DayViewWeekDataHolder
        private lateinit var BWeekDataHolder: DayViewWeekDataHolder
        private lateinit var subjects: List<Subject>

        fun getDayViewDataHolderFrom(subjects: List<Subject>): DayViewDataHolder {
            this.subjects = subjects

            assignSubjects()
            sortSubjectsByStartTime()

            return DayViewDataHolder(
                AWeekDataHolder,
                BWeekDataHolder,
                this.subjects
            )
        }

        private fun assignSubjects() {
            AWeekDataHolder = DayViewWeekDataHolder()
            BWeekDataHolder = DayViewWeekDataHolder()

            for (subject in subjects) {
                if (CalendarUtils.getWeekNumber(subject.startDate) == 0)
                    assignWeekASubject(subject)
                else
                    assignWeekBSubject(subject)
            }
        }

        private fun assignWeekASubject(subject: Subject) {
            when (CalendarUtils.getDayOfWeek(subject.startDate)) {
                0 -> AWeekDataHolder.mondaySubjects.add(SubjectItem(subject))
                1 -> AWeekDataHolder.tuesdaySubjects.add(SubjectItem(subject))
                2 -> AWeekDataHolder.wednesdaySubjects.add(SubjectItem(subject))
                3 -> AWeekDataHolder.thursdaySubjects.add(SubjectItem(subject))
                4 -> AWeekDataHolder.fridaySubjects.add(SubjectItem(subject))
                5 -> AWeekDataHolder.saturdaySubjects.add(SubjectItem(subject))
                6 -> AWeekDataHolder.sundaySubjects.add(SubjectItem(subject))
            }
        }

        private fun assignWeekBSubject(subject: Subject) {
            when (CalendarUtils.getDayOfWeek(subject.startDate)) {
                0 -> BWeekDataHolder.mondaySubjects.add(SubjectItem(subject))
                1 -> BWeekDataHolder.tuesdaySubjects.add(SubjectItem(subject))
                2 -> BWeekDataHolder.wednesdaySubjects.add(SubjectItem(subject))
                3 -> BWeekDataHolder.thursdaySubjects.add(SubjectItem(subject))
                4 -> BWeekDataHolder.fridaySubjects.add(SubjectItem(subject))
                5 -> BWeekDataHolder.saturdaySubjects.add(SubjectItem(subject))
                6 -> BWeekDataHolder.sundaySubjects.add(SubjectItem(subject))
            }
        }

        private fun sortSubjectsByStartTime() {
            AWeekDataHolder.mondaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.tuesdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.wednesdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.thursdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.fridaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.saturdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            AWeekDataHolder.sundaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.mondaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.tuesdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.wednesdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.thursdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.fridaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.saturdaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
            BWeekDataHolder.sundaySubjects.sortBy { CalendarUtils.getMinutesFromTimeString(it.startTime) }
        }
    }
}
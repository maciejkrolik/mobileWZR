package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.domain.domainmodel.TimetableDataHolder
import pl.expert.mobilewzr.domain.domainmodel.SubjectItem
import pl.expert.mobilewzr.domain.domainmodel.WeekDataHolder
import pl.expert.mobilewzr.data.model.Subject

abstract class TimetableViewUtils {

    companion object {

        private lateinit var AWeekDataHolder: WeekDataHolder
        private lateinit var BWeekDataHolder: WeekDataHolder
        private lateinit var subjects: List<Subject>

        fun getDayViewDataHolderFrom(subjects: List<Subject>): TimetableDataHolder {
            this.subjects = subjects

            assignSubjects()
            sortSubjectsByStartTime()

            return TimetableDataHolder(
                AWeekDataHolder,
                BWeekDataHolder,
                this.subjects
            )
        }

        private fun assignSubjects() {
            AWeekDataHolder = WeekDataHolder()
            BWeekDataHolder = WeekDataHolder()

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
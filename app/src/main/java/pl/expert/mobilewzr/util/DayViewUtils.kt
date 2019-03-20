package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.dto.DayViewDataHolder
import pl.expert.mobilewzr.data.dto.DayViewItem
import pl.expert.mobilewzr.data.dto.DayViewWeekDataHolder
import pl.expert.mobilewzr.data.model.Subject

abstract class DayViewUtils {

    companion object {

        private var AWeekDataHolder = DayViewWeekDataHolder()
        private var BWeekDataHolder = DayViewWeekDataHolder()

        private lateinit var subjects: List<Subject>

        private var indexOfLastSubjectInFirstWeek = 0

        fun getDayViewDataHolderFrom(subjects: List<Subject>): DayViewDataHolder {
            this.subjects = subjects

            fillAWeekDataHolder()
            fillBWeekDataHolder()

            return DayViewDataHolder(AWeekDataHolder, BWeekDataHolder)
        }

        private fun fillAWeekDataHolder() {
            AWeekDataHolder = DayViewWeekDataHolder()

            for (subjectIndex in 0 until subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[subjectIndex].startDate) == 0) {
                    when (CalendarUtils.getDayOfWeek(subjects[subjectIndex].startDate)) {
                        0 -> AWeekDataHolder.mondaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        1 -> AWeekDataHolder.tuesdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        2 -> AWeekDataHolder.wednesdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        3 -> AWeekDataHolder.thursdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        4 -> AWeekDataHolder.fridaySubjects.add(DayViewItem(subjects[subjectIndex]))
                    }
                    indexOfLastSubjectInFirstWeek = subjectIndex
                } else {
                    break
                }
            }
        }

        private fun fillBWeekDataHolder() {
            BWeekDataHolder = DayViewWeekDataHolder()

            for (subjectIndex in indexOfLastSubjectInFirstWeek + 1 until subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[subjectIndex].startDate) == 1) {
                    when (CalendarUtils.getDayOfWeek(subjects[subjectIndex].startDate)) {
                        0 -> BWeekDataHolder.mondaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        1 -> BWeekDataHolder.tuesdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        2 -> BWeekDataHolder.wednesdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        3 -> BWeekDataHolder.thursdaySubjects.add(DayViewItem(subjects[subjectIndex]))
                        4 -> BWeekDataHolder.fridaySubjects.add(DayViewItem(subjects[subjectIndex]))
                    }
                } else {
                    break
                }
            }
        }
    }
}
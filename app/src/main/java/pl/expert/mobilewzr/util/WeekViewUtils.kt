package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.dto.WeekViewSubjectItem
import pl.expert.mobilewzr.data.model.Subject

abstract class WeekViewUtils {

    companion object {

        private lateinit var weekViewItems: MutableList<WeekViewItem>
        private lateinit var subjects: List<Subject>

        private val timeStrings = listOf(
            "08.00 09.30",
            "09.45 11.15",
            "11.30 13.00",
            "13.30 15.00",
            "15.15 16.45",
            "17.00 18.30",
            "18.45 20.15"
        )

        /**
         * Returns thirty element list of WeekViewItems with times and subject titles of the first two weeks in semester.
         * Positions 0-14 - first week
         * Positions 14-29 - second week
         */
        fun getWeekViewItemsFrom(subjects: List<Subject>): List<WeekViewItem> {
            this.subjects = subjects

            fillWeekViewItemsListWithEmptyWeekViewItems()
            assignSubjects()

            return weekViewItems
        }

        private fun fillWeekViewItemsListWithEmptyWeekViewItems() {
            weekViewItems = mutableListOf()

            repeat(2) {
                timeStrings.forEach { timeString ->
                    val weekViewItem = WeekViewItem(
                        timeString,
                        MutableList(5) { WeekViewSubjectItem(-1, "") }
                    )
                    weekViewItems.add(weekViewItem)
                }
            }
        }

        /**
         * RecyclerView table contains six columns. One for each day and one for time.
         * This method assigns title and index of a subject to a specific WeekViewSubjectItem.
         */
        private fun assignSubjects() {
            for (subject in subjects) {
                if (CalendarUtils.getWeekNumber(subject.startDate) == 0)
                    assignSubject(subject, 0)
                else
                    assignSubject(subject, 7)
            }
        }

        private fun assignSubject(subject: Subject, shiftValue: Int) {
            when (subject.startTime) {
                "08.00" -> assignAt(0 + shiftValue, subject.index)
                "09.45" -> assignAt(1 + shiftValue, subject.index)
                "11.30" -> assignAt(2 + shiftValue, subject.index)
                "13.30" -> assignAt(3 + shiftValue, subject.index)
                "15.15" -> assignAt(4 + shiftValue, subject.index)
                "17.00" -> assignAt(5 + shiftValue, subject.index)
                "18.45" -> assignAt(6 + shiftValue, subject.index)
            }
        }

        private fun assignAt(weekViewItemPosition: Int, subjectIndex: Int) {
            val subject = subjects.single { subject -> subject.index == subjectIndex }
            weekViewItems[weekViewItemPosition].weekViewSubjectItems[CalendarUtils.getDayOfWeek(subject.startDate)] =
                WeekViewSubjectItem(subject.index, subject.title)
        }
    }
}
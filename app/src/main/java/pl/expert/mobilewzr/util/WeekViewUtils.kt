package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.dto.WeekViewSubjectItem
import pl.expert.mobilewzr.data.model.Subject

abstract class WeekViewUtils {

    companion object {

        private val weekViewItems: MutableList<WeekViewItem> = mutableListOf()

        private lateinit var subjects: List<Subject>
        private var indexOfLastSubjectInFirstWeek = 0

        private val timeStrings = listOf(
            "08.00 08.45",
            "08.45 09.30",
            "09.45 10.30",
            "10.30 11.15",
            "11.30 12.15",
            "12.15 13.00",
            "13.30 14.15",
            "14.15 15.00",
            "15.15 16.00",
            "16.00 16.45",
            "17.00 17.45",
            "17.45 18.30",
            "18.45 19.30",
            "19.30 20.15",
            "20.15 21.00"
        )

        /**
         * Returns thirty element list of WeekViewItems with times and subject titles of the first two weeks in semester.
         * Positions 0-14 - first week
         * Positions 14-29 - second week
         */
        fun getWeekViewItemsFrom(subjects: List<Subject>): List<WeekViewItem> {
            this.subjects = subjects

            fillWeekViewItemsListWithEmptyWeekViewItems()
            assignWeekASubjects()
            assignWeekBSubjects()

            return weekViewItems
        }

        private fun assignWeekASubjects() {
            for (subjectIndex in 0 until subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[subjectIndex].startDate) == 0) {
                    when (subjects[subjectIndex].startTime) {
                        "08.00" -> assignSubject(0, subjectIndex)
                        "08.45" -> assignSubject(1, subjectIndex)
                        "09.45" -> assignSubject(2, subjectIndex)
                        "10.30" -> assignSubject(3, subjectIndex)
                        "11.30" -> assignSubject(4, subjectIndex)
                        "12.15" -> assignSubject(5, subjectIndex)
                        "13.30" -> assignSubject(6, subjectIndex)
                        "14.15" -> assignSubject(7, subjectIndex)
                        "15.15" -> assignSubject(8, subjectIndex)
                        "16.00" -> assignSubject(9, subjectIndex)
                        "17.00" -> assignSubject(10, subjectIndex)
                        "17.45" -> assignSubject(11, subjectIndex)
                        "18.45" -> assignSubject(12, subjectIndex)
                        "19.30" -> assignSubject(13, subjectIndex)
                        "20.15" -> assignSubject(14, subjectIndex)
                    }
                    indexOfLastSubjectInFirstWeek = subjectIndex
                } else {
                    break
                }
            }
        }

        private fun assignWeekBSubjects() {
            for (subjectIndex in indexOfLastSubjectInFirstWeek + 1 until subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[subjectIndex].startDate) == 1) {
                    when (subjects[subjectIndex].startTime) {
                        "08.00" -> assignSubject(15, subjectIndex)
                        "08.45" -> assignSubject(16, subjectIndex)
                        "09.45" -> assignSubject(17, subjectIndex)
                        "10.30" -> assignSubject(18, subjectIndex)
                        "11.30" -> assignSubject(19, subjectIndex)
                        "12.15" -> assignSubject(20, subjectIndex)
                        "13.30" -> assignSubject(21, subjectIndex)
                        "14.15" -> assignSubject(22, subjectIndex)
                        "15.15" -> assignSubject(23, subjectIndex)
                        "16.00" -> assignSubject(24, subjectIndex)
                        "17.00" -> assignSubject(25, subjectIndex)
                        "17.45" -> assignSubject(26, subjectIndex)
                        "18.45" -> assignSubject(27, subjectIndex)
                        "19.30" -> assignSubject(28, subjectIndex)
                        "20.15" -> assignSubject(29, subjectIndex)
                    }
                } else {
                    break
                }
            }
        }

        private fun fillWeekViewItemsListWithEmptyWeekViewItems() {
            weekViewItems.clear()

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
        private fun assignSubject(weekViewItemPosition: Int, subjectIndex: Int) {
            weekViewItems[weekViewItemPosition].weekViewSubjectItems[CalendarUtils.getDayOfWeek(subjects[subjectIndex].startDate)] =
                WeekViewSubjectItem(subjects[subjectIndex].index, subjects[subjectIndex].title)
        }
    }
}
package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.model.WeekViewItem

abstract class WeekViewUtils {

    companion object {

        private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()

        /**
         * Returns thirty element list of week view items with times and subject titles of the first two weeks in semester.
         * Positions 0-14 - first week
         * Positions 14-29 - second week
         */
        fun getListOfWeekViewItems(listOfSubjects: List<Subject>): List<WeekViewItem> {
            listOfWeekViewItems.clear()
            listOfWeekViewItems.addAll(getEmptyListOfWeekViewItems())

            var indexOfLastSubjectInFirstWeek = 0

            for (i in 0..listOfSubjects.size) {
                if (CalendarUtils.getWeekNumber(listOfSubjects[i].startDate) == 0) {
                    when (listOfSubjects[i].startTime) {
                        "08.00" -> setSubjectTitle(0, listOfSubjects[i])
                        "08.45" -> setSubjectTitle(1, listOfSubjects[i])
                        "09.45" -> setSubjectTitle(2, listOfSubjects[i])
                        "10.30" -> setSubjectTitle(3, listOfSubjects[i])
                        "11.30" -> setSubjectTitle(4, listOfSubjects[i])
                        "12.15" -> setSubjectTitle(5, listOfSubjects[i])
                        "13.30" -> setSubjectTitle(6, listOfSubjects[i])
                        "14.15" -> setSubjectTitle(7, listOfSubjects[i])
                        "15.15" -> setSubjectTitle(8, listOfSubjects[i])
                        "16.00" -> setSubjectTitle(9, listOfSubjects[i])
                        "17.00" -> setSubjectTitle(10, listOfSubjects[i])
                        "17.45" -> setSubjectTitle(11, listOfSubjects[i])
                        "18.45" -> setSubjectTitle(12, listOfSubjects[i])
                        "19.30" -> setSubjectTitle(13, listOfSubjects[i])
                        "20.15" -> setSubjectTitle(14, listOfSubjects[i])
                    }
                    indexOfLastSubjectInFirstWeek = i
                } else {
                    break
                }
            }

            listOfWeekViewItems.addAll(getEmptyListOfWeekViewItems())

            val secondWeekNumber =
                CalendarUtils.getWeekNumber(listOfSubjects[indexOfLastSubjectInFirstWeek + 1].startDate)

            for (i in indexOfLastSubjectInFirstWeek + 1..listOfSubjects.size) {
                if (CalendarUtils.getWeekNumber(listOfSubjects[i].startDate) == secondWeekNumber) {
                    when (listOfSubjects[i].startTime) {
                        "08.00" -> setSubjectTitle(15, listOfSubjects[i])
                        "08.45" -> setSubjectTitle(16, listOfSubjects[i])
                        "09.45" -> setSubjectTitle(17, listOfSubjects[i])
                        "10.30" -> setSubjectTitle(18, listOfSubjects[i])
                        "11.30" -> setSubjectTitle(19, listOfSubjects[i])
                        "12.15" -> setSubjectTitle(20, listOfSubjects[i])
                        "13.30" -> setSubjectTitle(21, listOfSubjects[i])
                        "14.15" -> setSubjectTitle(22, listOfSubjects[i])
                        "15.15" -> setSubjectTitle(23, listOfSubjects[i])
                        "16.00" -> setSubjectTitle(24, listOfSubjects[i])
                        "17.00" -> setSubjectTitle(25, listOfSubjects[i])
                        "17.45" -> setSubjectTitle(26, listOfSubjects[i])
                        "18.45" -> setSubjectTitle(27, listOfSubjects[i])
                        "19.30" -> setSubjectTitle(28, listOfSubjects[i])
                        "20.15" -> setSubjectTitle(29, listOfSubjects[i])
                    }
                } else {
                    break
                }
            }

            return listOfWeekViewItems
        }

        /**
         * Returns fifteen element list of week view items containing only times with subject title's text views empty.
         */
        private fun getEmptyListOfWeekViewItems(): List<WeekViewItem> {
            val list: MutableList<WeekViewItem> = mutableListOf()
            val timeStrings: List<String> =
                listOf(
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

            for (timeString in timeStrings) {
                val weekViewItem = WeekViewItem().apply {
                    time = timeString
                    listOfSubjects = listOf("", "", "", "", "")
                }
                list.add(weekViewItem)
            }

            return list
        }

        /**
         * RecyclerView table contains six columns. One for each day and one for time.
         * This method assign subject title to specific column's text view.
         */
        private fun setSubjectTitle(position: Int, subject: Subject) {
            listOfWeekViewItems[position].listOfSubjects[CalendarUtils.getDayOfWeek(subject.startDate)] = subject.title
        }
    }
}
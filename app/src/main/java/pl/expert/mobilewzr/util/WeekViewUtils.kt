package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.model.WeekViewItem

abstract class WeekViewUtils {

    companion object {

        private val listOfWeekViewItems: MutableList<WeekViewItem> = mutableListOf()

        /**
         * Returns fifteen element list of week view items with times and subject titles of the first week in semester.
         */
        fun getListOfWeekViewItems(listOfSubjects: List<Subject>): List<WeekViewItem> {
            listOfWeekViewItems.addAll(getEmptyListOfWeekViewItems())

            for (subject in listOfSubjects) {
                if (CalendarUtils.getWeekNumber(subject.startDate) == 0) {
                    when (subject.startTime) {
                        "08.00" -> setSubjectTitle(0, subject)
                        "08.45" -> setSubjectTitle(1, subject)
                        "09.45" -> setSubjectTitle(2, subject)
                        "10.30" -> setSubjectTitle(3, subject)
                        "11.30" -> setSubjectTitle(4, subject)
                        "12.15" -> setSubjectTitle(5, subject)
                        "13.30" -> setSubjectTitle(6, subject)
                        "14.15" -> setSubjectTitle(7, subject)
                        "15.15" -> setSubjectTitle(8, subject)
                        "16.00" -> setSubjectTitle(9, subject)
                        "17.00" -> setSubjectTitle(10, subject)
                        "17.45" -> setSubjectTitle(11, subject)
                        "18.45" -> setSubjectTitle(12, subject)
                        "19.30" -> setSubjectTitle(13, subject)
                        "20.15" -> setSubjectTitle(14, subject)
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
package pl.expert.mobilewzr.util

import android.util.Log
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

            val fixedListOfSubjects = fixSubjects(listOfSubjects)

            var indexOfLastSubjectInFirstWeek = 0

            for (i in 0..fixedListOfSubjects.size) {
                if (CalendarUtils.getWeekNumber(fixedListOfSubjects[i].startDate) == 0) {
                    when (fixedListOfSubjects[i].startTime) {
                        "08.00" -> setSubjectTitle(0, fixedListOfSubjects[i])
                        "08.45" -> setSubjectTitle(1, fixedListOfSubjects[i])
                        "09.45" -> setSubjectTitle(2, fixedListOfSubjects[i])
                        "10.30" -> setSubjectTitle(3, fixedListOfSubjects[i])
                        "11.30" -> setSubjectTitle(4, fixedListOfSubjects[i])
                        "12.15" -> setSubjectTitle(5, fixedListOfSubjects[i])
                        "13.30" -> setSubjectTitle(6, fixedListOfSubjects[i])
                        "14.15" -> setSubjectTitle(7, fixedListOfSubjects[i])
                        "15.15" -> setSubjectTitle(8, fixedListOfSubjects[i])
                        "16.00" -> setSubjectTitle(9, fixedListOfSubjects[i])
                        "17.00" -> setSubjectTitle(10, fixedListOfSubjects[i])
                        "17.45" -> setSubjectTitle(11, fixedListOfSubjects[i])
                        "18.45" -> setSubjectTitle(12, fixedListOfSubjects[i])
                        "19.30" -> setSubjectTitle(13, fixedListOfSubjects[i])
                        "20.15" -> setSubjectTitle(14, fixedListOfSubjects[i])
                    }
                    indexOfLastSubjectInFirstWeek = i
                } else {
                    break
                }
            }

            listOfWeekViewItems.addAll(getEmptyListOfWeekViewItems())

            val secondWeekNumber =
                CalendarUtils.getWeekNumber(fixedListOfSubjects[indexOfLastSubjectInFirstWeek + 1].startDate)

            for (i in indexOfLastSubjectInFirstWeek + 1..fixedListOfSubjects.size) {
                if (CalendarUtils.getWeekNumber(fixedListOfSubjects[i].startDate) == secondWeekNumber) {
                    when (fixedListOfSubjects[i].startTime) {
                        "08.00" -> setSubjectTitle(15, fixedListOfSubjects[i])
                        "08.45" -> setSubjectTitle(16, fixedListOfSubjects[i])
                        "09.45" -> setSubjectTitle(17, fixedListOfSubjects[i])
                        "10.30" -> setSubjectTitle(18, fixedListOfSubjects[i])
                        "11.30" -> setSubjectTitle(19, fixedListOfSubjects[i])
                        "12.15" -> setSubjectTitle(20, fixedListOfSubjects[i])
                        "13.30" -> setSubjectTitle(21, fixedListOfSubjects[i])
                        "14.15" -> setSubjectTitle(22, fixedListOfSubjects[i])
                        "15.15" -> setSubjectTitle(23, fixedListOfSubjects[i])
                        "16.00" -> setSubjectTitle(24, fixedListOfSubjects[i])
                        "17.00" -> setSubjectTitle(25, fixedListOfSubjects[i])
                        "17.45" -> setSubjectTitle(26, fixedListOfSubjects[i])
                        "18.45" -> setSubjectTitle(27, fixedListOfSubjects[i])
                        "19.30" -> setSubjectTitle(28, fixedListOfSubjects[i])
                        "20.15" -> setSubjectTitle(29, fixedListOfSubjects[i])
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
                val weekViewItem = WeekViewItem(timeString, mutableListOf("", "", "", "", ""))
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

        /**
         * Some two hour subjects take two lines in CSV file but others take only one line.
         * This function fixes this problem by adding additional subject when needed.
         */
        private fun fixSubjects(listOfSubjects: List<Subject>): List<Subject> {
            val fixedListOfSubjects = listOfSubjects.toMutableList()

            var wasSecondWeek = false
            var pastSubject = Subject()
            val iterator = fixedListOfSubjects.listIterator()
            while (iterator.hasNext()) {
                val subject = iterator.next()

                if (CalendarUtils.getWeekNumber(subject.startDate) == 1) wasSecondWeek = true
                if (CalendarUtils.getWeekNumber(subject.startDate) == 0 && wasSecondWeek) break

                if (subject.title != pastSubject.title &&
                    subject.title != fixedListOfSubjects[iterator.nextIndex()].title
                ) {
                    val previousIteratorIndex = iterator.previousIndex()
                    val previousSubject = fixedListOfSubjects[previousIteratorIndex]

                    val newSubject = previousSubject.copy(
                        startTime = CalendarUtils.convertMinutesToTimeString(
                            CalendarUtils.getMinutesFromTimeString(
                                previousSubject.endTime
                            ) - 45
                        )
                    )
                    val modifiedPreviousSubject = previousSubject.copy(
                        endTime = CalendarUtils.convertMinutesToTimeString(
                            CalendarUtils.getMinutesFromTimeString(
                                previousSubject.startTime + 45
                            )
                        )
                    )

                    iterator.set(modifiedPreviousSubject)
                    iterator.add(newSubject)
                }

                pastSubject = subject
            }

            return fixedListOfSubjects
        }
    }
}
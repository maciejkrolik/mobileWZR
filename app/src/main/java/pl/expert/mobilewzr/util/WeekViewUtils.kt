package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.dto.WeekViewSubjectItem

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

            if (listOfSubjects.isEmpty()) {
                return listOfWeekViewItems
            }

            var indexOfLastSubjectInFirstWeek = 0
            var subjectCsvPosition = 0

            for (i in 0..listOfSubjects.size) {
                if (CalendarUtils.getWeekNumber(listOfSubjects[i].startDate) == 0) {
                    when (listOfSubjects[i].startTime) {
                        "08.00" -> setSubject(0, subjectCsvPosition, listOfSubjects[i])
                        "08.45" -> setSubject(1, subjectCsvPosition, listOfSubjects[i])
                        "09.45" -> setSubject(2, subjectCsvPosition, listOfSubjects[i])
                        "10.30" -> setSubject(3, subjectCsvPosition, listOfSubjects[i])
                        "11.30" -> setSubject(4, subjectCsvPosition, listOfSubjects[i])
                        "12.15" -> setSubject(5, subjectCsvPosition, listOfSubjects[i])
                        "13.30" -> setSubject(6, subjectCsvPosition, listOfSubjects[i])
                        "14.15" -> setSubject(7, subjectCsvPosition, listOfSubjects[i])
                        "15.15" -> setSubject(8, subjectCsvPosition, listOfSubjects[i])
                        "16.00" -> setSubject(9, subjectCsvPosition, listOfSubjects[i])
                        "17.00" -> setSubject(10, subjectCsvPosition, listOfSubjects[i])
                        "17.45" -> setSubject(11, subjectCsvPosition, listOfSubjects[i])
                        "18.45" -> setSubject(12, subjectCsvPosition, listOfSubjects[i])
                        "19.30" -> setSubject(13, subjectCsvPosition, listOfSubjects[i])
                        "20.15" -> setSubject(14, subjectCsvPosition, listOfSubjects[i])
                    }
                    indexOfLastSubjectInFirstWeek = i
                    subjectCsvPosition++
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
                        "08.00" -> setSubject(15, subjectCsvPosition, listOfSubjects[i])
                        "08.45" -> setSubject(16, subjectCsvPosition, listOfSubjects[i])
                        "09.45" -> setSubject(17, subjectCsvPosition, listOfSubjects[i])
                        "10.30" -> setSubject(18, subjectCsvPosition, listOfSubjects[i])
                        "11.30" -> setSubject(19, subjectCsvPosition, listOfSubjects[i])
                        "12.15" -> setSubject(20, subjectCsvPosition, listOfSubjects[i])
                        "13.30" -> setSubject(21, subjectCsvPosition, listOfSubjects[i])
                        "14.15" -> setSubject(22, subjectCsvPosition, listOfSubjects[i])
                        "15.15" -> setSubject(23, subjectCsvPosition, listOfSubjects[i])
                        "16.00" -> setSubject(24, subjectCsvPosition, listOfSubjects[i])
                        "17.00" -> setSubject(25, subjectCsvPosition, listOfSubjects[i])
                        "17.45" -> setSubject(26, subjectCsvPosition, listOfSubjects[i])
                        "18.45" -> setSubject(27, subjectCsvPosition, listOfSubjects[i])
                        "19.30" -> setSubject(28, subjectCsvPosition, listOfSubjects[i])
                        "20.15" -> setSubject(29, subjectCsvPosition, listOfSubjects[i])
                    }
                    subjectCsvPosition++
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
                val weekViewItem = WeekViewItem(
                    timeString,
                    mutableListOf(
                        WeekViewSubjectItem(-1, ""),
                        WeekViewSubjectItem(-1, ""),
                        WeekViewSubjectItem(-1, ""),
                        WeekViewSubjectItem(-1, ""),
                        WeekViewSubjectItem(-1, "")
                    )
                )
                list.add(weekViewItem)
            }

            return list
        }

        /**
         * RecyclerView table contains six columns. One for each day and one for time.
         * This method assign title and CSV position of a subject to specific week view item.
         */
        private fun setSubject(position: Int, csvPosition: Int, subject: Subject) {
            listOfWeekViewItems[position].listOfSubjects[CalendarUtils.getDayOfWeek(subject.startDate)] =
                WeekViewSubjectItem(csvPosition, subject.title)
        }

        /**
         * Some two hour subjects take two lines in CSV file but others take only one line.
         * This function fixes this problem by adding additional subject when needed.
         */
        fun fixSubjects(listOfSubjects: List<Subject>): List<Subject> {
            val fixedListOfSubjects = listOfSubjects.toMutableList()

            fixedListOfSubjects.removeAll { subject -> subject.title.isNullOrEmpty() }

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
                        startTime = CalendarUtils.addMinutesToTimeString(previousSubject.endTime, -45)
                    )
                    val modifiedPreviousSubject = previousSubject.copy(
                        endTime = CalendarUtils.addMinutesToTimeString(previousSubject.startTime, 45)
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
package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.dto.WeekViewItem
import pl.expert.mobilewzr.data.dto.WeekViewSubjectItem
import pl.expert.mobilewzr.data.model.Subject

abstract class WeekViewUtils {

    companion object {

        private val weekViewItems: MutableList<WeekViewItem> = mutableListOf()

        /**
         * Returns thirty element list of week view items with times and subject titles of the first two weeks in semester.
         * Positions 0-14 - first week
         * Positions 14-29 - second week
         */
        fun getWeekViewItems(subjects: List<Subject>): List<WeekViewItem> {
            weekViewItems.clear()
            weekViewItems.addAll(getListOfEmptyWeekViewItems())

            if (subjects.isEmpty()) {
                return weekViewItems
            }

            var indexOfLastSubjectInFirstWeek = 0
            var subjectCsvIndex = 0

            for (i in 0..subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[i].startDate) == 0) {
                    when (subjects[i].startTime) {
                        "08.00" -> setSubject(0, subjects[i])
                        "08.45" -> setSubject(1, subjects[i])
                        "09.45" -> setSubject(2, subjects[i])
                        "10.30" -> setSubject(3, subjects[i])
                        "11.30" -> setSubject(4, subjects[i])
                        "12.15" -> setSubject(5, subjects[i])
                        "13.30" -> setSubject(6, subjects[i])
                        "14.15" -> setSubject(7, subjects[i])
                        "15.15" -> setSubject(8, subjects[i])
                        "16.00" -> setSubject(9, subjects[i])
                        "17.00" -> setSubject(10, subjects[i])
                        "17.45" -> setSubject(11, subjects[i])
                        "18.45" -> setSubject(12, subjects[i])
                        "19.30" -> setSubject(13, subjects[i])
                        "20.15" -> setSubject(14, subjects[i])
                    }
                    indexOfLastSubjectInFirstWeek = i
                    subjectCsvIndex++
                } else {
                    break
                }
            }

            weekViewItems.addAll(getListOfEmptyWeekViewItems())

            val secondWeekNumber =
                CalendarUtils.getWeekNumber(subjects[indexOfLastSubjectInFirstWeek + 1].startDate)

            for (i in indexOfLastSubjectInFirstWeek + 1 until subjects.size) {
                if (CalendarUtils.getWeekNumber(subjects[i].startDate) == secondWeekNumber) {
                    when (subjects[i].startTime) {
                        "08.00" -> setSubject(15, subjects[i])
                        "08.45" -> setSubject(16, subjects[i])
                        "09.45" -> setSubject(17, subjects[i])
                        "10.30" -> setSubject(18, subjects[i])
                        "11.30" -> setSubject(19, subjects[i])
                        "12.15" -> setSubject(20, subjects[i])
                        "13.30" -> setSubject(21, subjects[i])
                        "14.15" -> setSubject(22, subjects[i])
                        "15.15" -> setSubject(23, subjects[i])
                        "16.00" -> setSubject(24, subjects[i])
                        "17.00" -> setSubject(25, subjects[i])
                        "17.45" -> setSubject(26, subjects[i])
                        "18.45" -> setSubject(27, subjects[i])
                        "19.30" -> setSubject(28, subjects[i])
                        "20.15" -> setSubject(29, subjects[i])
                    }
                    subjectCsvIndex++
                } else {
                    break
                }
            }

            return weekViewItems
        }

        /**
         * Returns fifteen element list of week view items containing only times with subject title's text views empty.
         */
        private fun getListOfEmptyWeekViewItems(): List<WeekViewItem> {
            val emptyWeekViewItems: MutableList<WeekViewItem> = mutableListOf()
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
                emptyWeekViewItems.add(weekViewItem)
            }

            return emptyWeekViewItems
        }

        /**
         * RecyclerView table contains six columns. One for each day and one for time.
         * This method assign title and CSV index of a subject to specific week view item.
         */
        private fun setSubject(position: Int, subject: Subject) {
            weekViewItems[position].weekViewSubjectItems[CalendarUtils.getDayOfWeek(subject.startDate)] =
                WeekViewSubjectItem(subject.csvIndex, subject.title)
        }

        /**
         * Some two hour subjects take two lines in CSV file but others take only one line.
         * This function fixes this problem by adding additional subject when needed and adds csv index to subjects
         */
        fun fixSubjects(subjects: List<Subject>): List<Subject> {
            val fixedSubjects = subjects.toMutableList()

            fixedSubjects.removeAll { subject -> subject.title.isNullOrEmpty() }

            var wasSecondWeek = false
            var pastSubject = Subject()
            var csvIndex = 0
            val iterator = fixedSubjects.listIterator()
            while (iterator.hasNext()) {
                val subject = iterator.next()
                if (CalendarUtils.getWeekNumber(subject.startDate) == 1) wasSecondWeek = true
                if (CalendarUtils.getWeekNumber(subject.startDate) == 0 && wasSecondWeek) break

                subject.csvIndex = csvIndex

                if (subject.title != pastSubject.title &&
                    subject.title != fixedSubjects[iterator.nextIndex()].title
                ) {
                    val previousIteratorIndex = iterator.previousIndex()
                    val previousSubject = fixedSubjects[previousIteratorIndex]

                    csvIndex++

                    val newSubject = previousSubject.copy(
                        csvIndex = csvIndex,
                        startTime = CalendarUtils.addMinutesToTimeString(previousSubject.endTime, -45)
                    )
                    val modifiedPreviousSubject = previousSubject.copy(
                        endTime = CalendarUtils.addMinutesToTimeString(previousSubject.startTime, 45)
                    )

                    iterator.set(modifiedPreviousSubject)
                    iterator.add(newSubject)
                }

                pastSubject = subject

                csvIndex++
            }

            return fixedSubjects.take(csvIndex)
        }
    }
}
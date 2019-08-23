package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.Subject
import java.text.SimpleDateFormat
import java.util.*

abstract class SubjectsUtils {

    companion object {

        private lateinit var subjects: MutableList<Subject>

        /**
         * Returns allSubjects only from the first two weeks of the semester. Useful in full-time studies layouts.
         */
        fun getOnlyFirstTwoWeeksSubjectsFrom(subjects: List<Subject>): List<Subject> {
            val firstTwoWeeksSubjects: MutableList<Subject> = mutableListOf()
            val firstWeekNumber = SimpleDateFormat("w", Locale.UK).format(subjects.first().startDate).toInt()

            for (subject in subjects) {
                if (SimpleDateFormat("w", Locale.UK).format(subject.startDate).toInt() <= firstWeekNumber + 1) {
                    firstTwoWeeksSubjects.add(subject)
                } else {
                    break
                }
            }

            return firstTwoWeeksSubjects
        }

        /**
         * Some two hour allSubjects take two lines in CSV file but others take only one line.
         * This function assigns indexes to allSubjects and fixes above problem by adding additional subject when needed.
         */
        fun fix(subjects: List<Subject>): List<Subject> {
            this.subjects = subjects.toMutableList()

            deleteSubjectsWithoutTitle()
            addAdditionalSubjects()
            assignIndexesToSubjects()

            return this.subjects
        }

        /**
         * Merges doubled allSubjects into one with start time of the first and end time of the second. Useful in DayView.
         */
        fun mergeMultipleSubjectsIntoOne(subjects: List<Subject>): List<Subject> {
            val mergedSubjects = mutableListOf<Subject>()

            for (i in 0 until subjects.size step 2) {
                mergedSubjects.add(subjects[i])
                subjects[i].endTime = subjects.getOrNull(i + 1)?.endTime ?: subjects[i].endTime
            }

            return mergedSubjects
        }

        private fun deleteSubjectsWithoutTitle() {
            subjects.removeAll { subject -> subject.title.isEmpty() }
        }

        private fun addAdditionalSubjects() {
            var previousSubjectInALoop = Subject()
            val iterator = subjects.listIterator()
            while (iterator.hasNext()) {
                val subject = iterator.next()

                if (subject.title != previousSubjectInALoop.title &&
                    subject.title != subjects.getOrNull(iterator.nextIndex())?.title
                ) {
                    val previousSubject = subjects[iterator.previousIndex()]

                    val newSubject = previousSubject.copy(
                        startTime = CalendarUtils.addMinutesToTimeString(previousSubject.endTime, -45)
                    )
                    val modifiedPreviousSubject = previousSubject.copy(
                        endTime = CalendarUtils.addMinutesToTimeString(previousSubject.startTime, 45)
                    )

                    iterator.set(modifiedPreviousSubject)
                    iterator.add(newSubject)
                }

                previousSubjectInALoop = subject
            }
        }

        private fun assignIndexesToSubjects() {
            subjects.forEachIndexed { index, subject ->
                subject.index = index
            }
        }
    }
}
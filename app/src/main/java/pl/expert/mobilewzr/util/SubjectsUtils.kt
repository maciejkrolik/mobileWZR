package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.Subject

abstract class SubjectsUtils {

    companion object {

        private lateinit var subjects: MutableList<Subject>

        /**
         * Some two hour subjects take two lines in CSV file but others take only one line.
         * This function assigns indexes to subjects and fixes above problem by adding additional subject when needed.
         */
        fun fix(subjects: List<Subject>): List<Subject> {
            this.subjects = subjects.toMutableList()

            deleteSubjectsWithoutTitle()
            addAdditionalSubjects()
            assignIndexesToSubjects()

            return this.subjects
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
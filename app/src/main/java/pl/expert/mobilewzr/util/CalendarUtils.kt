package pl.expert.mobilewzr.util

import java.text.SimpleDateFormat
import java.util.*

abstract class CalendarUtils {

    companion object {

        /**
         * Returns day of the week of the given date
         * Monday - 0, Sunday - 6
         */
        fun getDayOfWeek(date: Date): Int {
            val calendar: Calendar = Calendar.getInstance(Locale.UK)
            calendar.time = date
            return calendar.get(Calendar.DAY_OF_WEEK) - 2
        }

        /**
         * Returns current day of the week
         * Monday - 0, Sunday - 6
         */
        fun getDayOfWeek(): Int {
            val calendar: Calendar = Calendar.getInstance(Locale.UK)
            return getDayOfWeek(calendar.time)
        }

        /**
         * Returns current time in time string
         */
        fun getCurrentTime(): String {
            val sdf = SimpleDateFormat("HH.mm", Locale.UK)
            return sdf.format(Calendar.getInstance().time)
        }

        /**
         * Returns week number/type of the given date
         * Week A - 0, Week B - 1
         */
        fun getWeekNumber(date: Date): Int {
            val calendar: Calendar = Calendar.getInstance(Locale.UK)
            calendar.time = date
            return if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                0
            } else {
                1
            }
        }

        /**
         * Returns current week number/type
         * Week A - 0, Week B - 1
         */
        fun getWeekNumber(): Int {
            val calendar: Calendar = Calendar.getInstance(Locale.UK)
            return getWeekNumber(calendar.time)
        }

        /**
         * Returns week type as a string (A/B) of the given week number
         */
        fun getWeekType(weekNumber: Int): String {
            return if (weekNumber == 0) "A" else "B"
        }

        /**
         * Converts time string for minutes
         * Time string format example: 08.00
         */
        fun getMinutesFromTimeString(time: String): Int {
            val timeSplit = time.split(".")
            return 60 * timeSplit[0].toInt() + timeSplit[1].toInt()
        }

        /**
         * Converts minutes to time string
         * Output example time string format: 08:00
         */
        fun convertMinutesToTimeString(minutes: Int): String {
            val h = minutes / 60
            val m = minutes - h * 60
            return "${h.toString().padStart(2, '0')}.${m.toString().padStart(2, '0')}"
        }

        /**
         * Returns time string increased by a passed minutes parameter
         */
        fun addMinutesToTimeString(timeString: String, minutesToAdd: Int) =
            convertMinutesToTimeString(getMinutesFromTimeString(timeString) + minutesToAdd)

        /**
         * Returns an index of the subject time.
         * 0 - 08.00
         * 1 - 08.45...
         */
        fun getSubjectTimeIndexFrom(subjectTime: String): Int {
            return when (subjectTime) {
                "08.00" -> 0
                "08.45" -> 1
                "09.45" -> 2
                "10.30" -> 3
                "11.30" -> 4
                "12.15" -> 5
                "13.30" -> 6
                "14.15" -> 7
                "15.15" -> 8
                "16.00" -> 9
                "17.00" -> 10
                "17.45" -> 11
                "18.45" -> 12
                "19.30" -> 13
                "20.15" -> 14
                else -> throw IllegalArgumentException("There is no subject time: $subjectTime")
            }
        }

        /**
         * Returns a subject time string based on the given position of the spinner.
         */
        fun getSubjectTimeStringFrom(index: Int): String {
            return when (index) {
                0 -> "08.00"
                1 -> "08.45"
                2 -> "09.45"
                3 -> "10.30"
                4 -> "11.30"
                5 -> "12.15"
                6 -> "13.30"
                7 -> "14.15"
                8 -> "15.15"
                9 -> "16.00"
                10 -> "17.00"
                11 -> "17.45"
                12 -> "18.45"
                13 -> "19.30"
                14 -> "20.15"
                else -> throw IllegalArgumentException("There is no index: $index")
            }
        }
    }
}


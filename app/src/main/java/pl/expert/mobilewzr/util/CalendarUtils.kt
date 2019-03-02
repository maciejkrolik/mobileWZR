package pl.expert.mobilewzr.util

import java.util.*

abstract class CalendarUtils {

    companion object {

        /**
         * Returns day of the week of the given date
         * Monday - 0, Sunday - 6
         */
        fun getDayOfWeek(date: Date): Int {
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.get(Calendar.DAY_OF_WEEK) - 2
        }

        /**
         * Returns week number/type of the given date
         * Week A - 0, Week B - 1
         */
        fun getWeekNumber(date: Date): Int {
            val calendar: Calendar = Calendar.getInstance()
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
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
            return getWeekNumber(calendar.time)
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
    }
}


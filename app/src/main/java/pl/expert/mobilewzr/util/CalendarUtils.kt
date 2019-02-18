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
         * Returns true when given date is in current week
         */
        fun isInCurrentWeek(date: Date): Boolean {
            val currentDate: Calendar = Calendar.getInstance()

            val inputDate: Calendar = Calendar.getInstance()
            inputDate.time = date

            if (currentDate.get(Calendar.YEAR) == inputDate.get(Calendar.YEAR)) {
                if (currentDate.get(Calendar.WEEK_OF_YEAR) == inputDate.get(Calendar.WEEK_OF_YEAR)) {
                    return true
                }
            }
            return false
        }
    }
}


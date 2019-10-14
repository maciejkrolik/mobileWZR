package pl.expert.mobilewzr.util

import java.text.SimpleDateFormat
import java.util.*

abstract class CalendarUtils {

    companion object {

        const val eightAmInMinutes = 480
        const val ninePmInMinutes = 1260

        /**
         * Returns calendar object with date set as today
         */
        fun getCalendar(): Calendar {
            val now = Date()
            val calendar = Calendar.getInstance(Locale.UK)
            calendar.time = now
            return calendar
        }

        /**
         * Returns day of the week of the given date
         * Monday - 0, Sunday - 6
         */
        fun getDayOfWeek(date: Date): Int {
            val calendar: Calendar = Calendar.getInstance(Locale.UK)
            calendar.time = date
            return if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) 6 else calendar.get(Calendar.DAY_OF_WEEK) - 2
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
         * Returns day of the week based on passed parameters
         * Monday - 0, Sunday - 6
         */
        fun getDayOfWeek(day: Int, month: Int, year: Int): Int {
            val calendar = Calendar.getInstance(Locale.UK)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
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

    }
}


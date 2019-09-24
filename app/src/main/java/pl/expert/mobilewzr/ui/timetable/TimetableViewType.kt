package pl.expert.mobilewzr.ui.timetable

enum class TimetableViewType(val value: Int) {

    DAY_VIEW(0),
    WEEK_VIEW(1),
    CALENDAR_VIEW(2);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }

}
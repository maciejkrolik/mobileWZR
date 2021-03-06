package pl.expert.mobilewzr.ui.timetable

enum class TimetableViewLocation(val value: Int) {

    MY_TIMETABLE(0),
    SEARCH(1);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }

}
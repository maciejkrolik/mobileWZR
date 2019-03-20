package pl.expert.mobilewzr.ui.timetableviews.weekview

enum class WeekViewLocation(val value: Int) {
    MY_TIMETABLE(0), SEARCH(1);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }

}
package pl.expert.mobilewzr.ui.timetableviews

enum class TimetableViewType(val value: Int) {
    DAY_VIEW(0), WEEK_VIEW(1);

    companion object {
        private val values = TimetableViewType.values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }
}
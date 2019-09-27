package pl.expert.mobilewzr.ui.timetable

enum class TimetableType(val value: Int) {

    FULL_TIME(0),
    PART_TIME(1);

    companion object {
        private val values = TimetableViewType.values()
        fun getByValue(value: Int) = values.first { it.value == value }
    }

}
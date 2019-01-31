package pl.expert.mobilewzr.util

import pl.expert.mobilewzr.data.model.WeekViewItem

class WeekViewUtils {

    companion object {

        fun getEmptyListOfWeekViewItemsWithTimes(): List<WeekViewItem> {
            val list: MutableList<WeekViewItem> = mutableListOf()
            val timeStrings: List<String> =
                listOf(
                    "08.00 08.45",
                    "08.45 09.30",
                    "09.45 10.30",
                    "10.30 11.15",
                    "11.30 12.15",
                    "12.15 13.00",
                    "13.30 14.15",
                    "14.15 15.00",
                    "15.15 16.00",
                    "16.00 16.45",
                    "17.00 17.45",
                    "17.45 18.30",
                    "18.45 19.30",
                    "19.30 20.15",
                    "20.15 21.00"
                )

            for (timeString in timeStrings) {
                val weekViewItem = WeekViewItem().apply {
                    time = timeString
                    listOfSubjects = listOf("", "", "", "", "")
                }
                list.add(weekViewItem)
            }

            return list
        }
    }
}
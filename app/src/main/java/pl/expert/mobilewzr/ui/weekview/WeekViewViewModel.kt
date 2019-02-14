package pl.expert.mobilewzr.ui.weekview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pl.expert.mobilewzr.data.SubjectsRepository
import pl.expert.mobilewzr.data.model.WeekViewItem

class WeekViewViewModel constructor(
    private val repository: SubjectsRepository
) : ViewModel() {

    private var listOfWeekViewItems = MutableLiveData<List<WeekViewItem>>()

    fun getSubjects(groupId: String): LiveData<List<WeekViewItem>> {
        if (listOfWeekViewItems.value.isNullOrEmpty()) {
            listOfWeekViewItems = repository.getListOfWeekViewItems(groupId)
        }
        return listOfWeekViewItems
    }
}
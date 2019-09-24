package pl.expert.mobilewzr.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository

class SearchViewModel constructor(
        private val repository: SubjectsRepository
) : ViewModel() {

    private var groups = MutableLiveData<List<String>>()
    private var previouslySelectedGroupIdIndex = 0

    fun getGroups(): LiveData<List<String>> {
        if (groups.value.isNullOrEmpty()) {
            viewModelScope.launch {
                groups.value = repository.getGroups()
            }
        }
        return groups
    }

    fun getGroupIdIndex() = previouslySelectedGroupIdIndex

    fun setGroupIdIndex(groupIdIndex: Int) {
        previouslySelectedGroupIdIndex = groupIdIndex
    }

}
package pl.expert.mobilewzr.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.SubjectsRepository

class SearchViewModel constructor(
        private val repository: SubjectsRepository
) : ViewModel() {

    private var groups = MutableLiveData<List<String>>()
    private var previouslySelectedGroupIdIndex = 0

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
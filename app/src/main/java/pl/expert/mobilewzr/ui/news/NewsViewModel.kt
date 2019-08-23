package pl.expert.mobilewzr.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.model.News

class NewsViewModel constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var news = MutableLiveData<List<News>>()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getNews(): LiveData<List<News>> {
        if (news.value.isNullOrEmpty()) {
            viewModelScope.launch {
                news.value = repository.getNews()
            }
        }
        return news
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
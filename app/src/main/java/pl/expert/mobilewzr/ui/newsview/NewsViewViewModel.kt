package pl.expert.mobilewzr.ui.newsview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.model.News

class NewsViewViewModel constructor(
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
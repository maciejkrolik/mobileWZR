package pl.expert.mobilewzr.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.expert.mobilewzr.data.NewsRepository
import pl.expert.mobilewzr.data.model.News

class NewsViewModel constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var news = MutableLiveData<List<News>>()

    fun getNews(): LiveData<List<News>> {
        if (news.value.isNullOrEmpty()) {
            viewModelScope.launch {
                news.value = repository.getNews()
            }
        }
        return news
    }

    fun reloadNews() {
        viewModelScope.launch {
            news.value = repository.getNews()
        }
    }

}
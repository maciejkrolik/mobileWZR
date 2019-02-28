package pl.expert.mobilewzr.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import pl.expert.mobilewzr.data.model.News
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "NewsRepository"

@Singleton
class NewsRepository @Inject constructor() {

    suspend fun getNews(): List<News> {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect("https://wzr.ug.edu.pl/studia/index.php?str=438").get()
        }

        Log.i(TAG, "News downloaded")

        val newsTitles = doc.getElementsByClass("blok_tytul")
        val newsContent = doc.getElementsByClass("ramka")

        val downloadedNews = newsTitles.zip(newsContent)

        val listOfNews: MutableList<News> = mutableListOf()

        downloadedNews.forEach { (title, content) ->
            listOfNews.add(News(title.ownText(), content.text()))
        }

        return listOfNews
    }
}
package pl.expert.mobilewzr.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "GroupsRepository"

@Singleton
class GroupsRepository @Inject constructor() {

    suspend fun getGroups(): List<String> {
        var doc = Document("")
        try {
            doc = withContext(Dispatchers.IO) {
                Jsoup.connect(URLs.GROUPS_URL).get()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        Log.i(TAG, "Groups downloaded")

        val groups = doc.select("select option:not(:first-child)")
        return groups.eachText()
    }

}
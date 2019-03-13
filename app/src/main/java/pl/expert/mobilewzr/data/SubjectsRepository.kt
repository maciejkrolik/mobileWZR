package pl.expert.mobilewzr.data

import androidx.lifecycle.MutableLiveData
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.SubjectsWithWeekViews
import pl.expert.mobilewzr.util.WeekViewUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SubjectsRepository"

@Singleton
class SubjectsRepository @Inject constructor(
    private val wzrService: WZRService
) {

    fun getSubjectsWithWeekViews(groupId: String): MutableLiveData<SubjectsWithWeekViews> {
        val subjectsWithWeekViews: MutableLiveData<SubjectsWithWeekViews> = MutableLiveData()

        wzrService.listSubjects(groupId).enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.body() != null) {
                    val listOfSubjects = WeekViewUtils.fixSubjects(response.body() as List<Subject>)
                    val listOfWeekViewItems = WeekViewUtils.getListOfWeekViewItems(listOfSubjects)

                    subjectsWithWeekViews.value = SubjectsWithWeekViews(listOfSubjects, listOfWeekViewItems)
                }

                Log.i(TAG, "Subjects successfully downloaded")
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                t.printStackTrace()

                Log.i(TAG, "Error downloading subjects")
            }
        })

        return subjectsWithWeekViews
    }

    suspend fun getGroups(): List<String> {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect("https://wzr.ug.edu.pl/studia/index.php?str=437").get()
        }

        Log.i(TAG, "Groups downloaded")

        val groups = doc.select("select option:not(:first-child)")

        return groups.eachText()
    }
}
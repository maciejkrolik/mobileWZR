package pl.expert.mobilewzr.data

import androidx.lifecycle.MutableLiveData
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.dto.WeekViewDataHolder
import pl.expert.mobilewzr.util.SubjectsUtils
import pl.expert.mobilewzr.util.WeekViewUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SubjectsRepository"

@Singleton
class SubjectsRepository @Inject constructor(
    private val wzrService: WZRService,
    private val subjectsDao: SubjectsDao
) {

    suspend fun replaceSubjectsInDb(subjects: List<Subject>) {
        subjectsDao.deleteSubjects()
        subjectsDao.addSubjects(subjects)

        Log.i(TAG, "Subjects saved to a database")
    }

    suspend fun getWeekViewDataFromDb(weekViewDataHolder: MutableLiveData<WeekViewDataHolder>): MutableLiveData<WeekViewDataHolder> {
        val subjects = subjectsDao.getSubjects()
        val weekViewItems = WeekViewUtils.getWeekViewItemsFrom(subjects)

        weekViewDataHolder.postValue(WeekViewDataHolder(subjects, weekViewItems))

        Log.i(TAG, "Subjects successfully retrieved from database")

        return weekViewDataHolder
    }

    fun getWeekViewDataFromService(groupId: String): MutableLiveData<WeekViewDataHolder> {
        val weekViewDataHolder: MutableLiveData<WeekViewDataHolder> = MutableLiveData()

        wzrService.getSubjects(groupId).enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                val downloadedSubjects = response.body()!!
                val fixedSubjects = SubjectsUtils.fix(downloadedSubjects)
                val weekViewItems = WeekViewUtils.getWeekViewItemsFrom(fixedSubjects)

                weekViewDataHolder.value = WeekViewDataHolder(fixedSubjects, weekViewItems)

                Log.i(TAG, "Subjects successfully downloaded")
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                t.printStackTrace()

                Log.i(TAG, "Error downloading subjects")
            }
        })

        return weekViewDataHolder
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
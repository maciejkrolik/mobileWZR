package pl.expert.mobilewzr.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pl.expert.mobilewzr.data.db.SubjectsDao
import pl.expert.mobilewzr.domain.domainmodel.DayViewDataHolder
import pl.expert.mobilewzr.domain.domainmodel.WeekViewDataHolder
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.util.DayViewUtils
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

    suspend fun getSubjectsFromDb(): List<Subject> {
        return subjectsDao.getSubjects()
    }

    suspend fun updateSubject(subject: Subject) {
        subjectsDao.updateSubjects(subject)
    }

    suspend fun updateSubjects(subjects: List<Subject>) {
        subjectsDao.updateSubjects(*subjects.toTypedArray())
    }

    suspend fun addSubject(subject: Subject) {
        subjectsDao.addSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject) {
        subjectsDao.deleteSubjects(subject)
    }

    suspend fun replaceSubjectsInDb(subjects: List<Subject>) {
        subjectsDao.deleteSubjects()
        subjectsDao.addSubjects(subjects)
    }

    suspend fun getDayViewDataFromDb(dayViewDataHolder: MutableLiveData<DayViewDataHolder>): MutableLiveData<DayViewDataHolder> {
        val subjects = subjectsDao.getSubjects()

        dayViewDataHolder.postValue(DayViewUtils.getDayViewDataHolderFrom(subjects))

        return dayViewDataHolder
    }

    suspend fun getWeekViewDataFromDb(weekViewDataHolder: MutableLiveData<WeekViewDataHolder>): MutableLiveData<WeekViewDataHolder> {
        val subjects = subjectsDao.getSubjects()

        weekViewDataHolder.postValue(WeekViewUtils.getWeekViewDataHolder(subjects))

        return weekViewDataHolder
    }

    fun getDayViewDataFromService(groupId: String): MutableLiveData<DayViewDataHolder> {
        val dayViewDataHolder: MutableLiveData<DayViewDataHolder> = MutableLiveData()

        wzrService.getSubjects(groupId).enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                val downloadedSubjects = response.body()!!
                val firstTwoWeeksSubjects = SubjectsUtils.getOnlyFirstTwoWeeksSubjectsFrom(downloadedSubjects)
                val fixedSubjects = SubjectsUtils.fix(firstTwoWeeksSubjects)
                val mergedSubjects = SubjectsUtils.mergeMultipleSubjectsIntoOne(fixedSubjects)

                dayViewDataHolder.value = DayViewUtils.getDayViewDataHolderFrom(mergedSubjects)

                Log.i(TAG, "Subjects successfully downloaded")
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                t.printStackTrace()

                Log.i(TAG, "Error downloading allSubjects")
            }
        })

        return dayViewDataHolder
    }

    fun getWeekViewDataFromService(groupId: String): MutableLiveData<WeekViewDataHolder> {
        val weekViewDataHolder: MutableLiveData<WeekViewDataHolder> = MutableLiveData()

        wzrService.getSubjects(groupId).enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                val downloadedSubjects = response.body()!!
                val firstTwoWeeksSubjects = SubjectsUtils.getOnlyFirstTwoWeeksSubjectsFrom(downloadedSubjects)
                val fixedSubjects = SubjectsUtils.fix(firstTwoWeeksSubjects)
                val mergedSubjects = SubjectsUtils.mergeMultipleSubjectsIntoOne(fixedSubjects)

                weekViewDataHolder.value = WeekViewUtils.getWeekViewDataHolder(mergedSubjects)

                Log.i(TAG, "Subjects successfully downloaded")
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                t.printStackTrace()

                Log.i(TAG, "Error downloading allSubjects")
            }
        })

        return weekViewDataHolder
    }

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
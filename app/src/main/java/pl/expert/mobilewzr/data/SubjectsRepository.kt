package pl.expert.mobilewzr.data

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.data.model.WeekViewItem
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

    fun getListOfWeekViewItems(groupId: String): MutableLiveData<List<WeekViewItem>> {
        val listOfWeekViewItems: MutableLiveData<List<WeekViewItem>> = MutableLiveData()

        wzrService.listSubjects(groupId).enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.body() != null) {
                    val parsedListOfWeekViewItems =
                        WeekViewUtils.getListOfWeekViewItems(response.body() as List<Subject>)
                    listOfWeekViewItems.value = parsedListOfWeekViewItems
                }

                Log.i(TAG, "Data successfully downloaded")
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                t.printStackTrace()

                Log.i(TAG, "Error downloading data")
            }
        })

        return listOfWeekViewItems
    }
}
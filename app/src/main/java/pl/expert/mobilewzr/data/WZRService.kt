package pl.expert.mobilewzr.data

import pl.expert.mobilewzr.data.model.Subject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WZRService {

    @GET("/.csv/plan_st.php?f2=4")
    fun getSubjects(@Query("f1") groupId: String): Call<List<Subject>>
}

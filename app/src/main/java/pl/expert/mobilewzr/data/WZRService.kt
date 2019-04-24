package pl.expert.mobilewzr.data

import pl.expert.mobilewzr.data.model.Subject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WZRService {

    @GET(URLs.SUBJECTS_PATH)
    fun getSubjects(@Query("f1") groupId: String): Call<List<Subject>>
}

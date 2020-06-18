package pl.expert.mobilewzr.data

import pl.expert.mobilewzr.data.model.Subject
import retrofit2.http.GET
import retrofit2.http.Query

interface WZRService {

    @GET(URLs.SUBJECTS_PATH)
    suspend fun getSubjects(
        @Query("f1") groupId: String,
        @Query("jp") params: String,
        @Query("f2") number: Int = 4
    ): List<Subject>

}
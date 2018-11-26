package pl.expert.mobilewzr.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface WzrService {

    @GET("/.csv/plan_st.php?f2=4")
    Call<List<Subject>> listSubjects(@Query("f1") String groupId);
}

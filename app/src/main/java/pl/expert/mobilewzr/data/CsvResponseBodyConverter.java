package pl.expert.mobilewzr.data;

import android.support.annotation.NonNull;
import com.univocity.parsers.csv.CsvRoutines;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvResponseBodyConverter implements Converter<ResponseBody, List<Subject>> {

    @Override
    public List<Subject> convert(@NonNull ResponseBody value) throws IOException {
        List<Subject> subjectList = new ArrayList<>();

        CsvRoutines routines = new CsvRoutines();
        for (Subject subject : routines.iterate(Subject.class, value.byteStream())) {
            subjectList.add(subject);
        }

        return subjectList;
    }
}

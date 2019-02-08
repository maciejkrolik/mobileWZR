package pl.expert.mobilewzr.data.converter;

import android.support.annotation.NonNull;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import okhttp3.ResponseBody;
import pl.expert.mobilewzr.data.model.Subject;
import retrofit2.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvResponseBodyConverter implements Converter<ResponseBody, List<Subject>> {

    @Override
    public List<Subject> convert(@NonNull ResponseBody value) throws IOException {
        List<Subject> subjectList = new ArrayList<>();

        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setNumberOfRowsToSkip(1);

        CsvRoutines routines = new CsvRoutines(parserSettings);
        for (Subject subject : routines.iterate(Subject.class, value.byteStream())) {
            subjectList.add(subject);
        }

        return subjectList;
    }
}

package pl.expert.mobilewzr.data.converter

import com.univocity.parsers.csv.CsvParserSettings
import com.univocity.parsers.csv.CsvRoutines
import okhttp3.ResponseBody
import pl.expert.mobilewzr.data.model.Subject
import retrofit2.Converter

class CsvResponseBodyConverter : Converter<ResponseBody, List<Subject>> {

    override fun convert(value: ResponseBody): List<Subject> {
        val subjects = mutableListOf<Subject>()

        val parserSettings = CsvParserSettings().apply {
            numberOfRowsToSkip = 1
        }

        val routines = CsvRoutines(parserSettings)
        for (subject in routines.iterate(Subject::class.java, value.byteStream())) {
            subjects.add(subject)
        }

        return subjects
    }

}
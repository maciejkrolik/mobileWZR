package pl.expert.mobilewzr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import pl.expert.mobilewzr.data.CsvConverterFactory
import pl.expert.mobilewzr.data.Subject
import pl.expert.mobilewzr.data.WzrService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text_view)

        val wzrService = Retrofit.Builder()
            .baseUrl("http://www.wzr.ug.edu.pl")
            .addConverterFactory(CsvConverterFactory.create())
            .build()
            .create(WzrService::class.java)

        wzrService.listSubjects("S22-31").enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                val test = response.body()

                var content = ""
                test?.forEach {
                    content += it.startDate
                }

                textView.text = content
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                Toast.makeText(applicationContext, "Błąd!", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })
    }
}

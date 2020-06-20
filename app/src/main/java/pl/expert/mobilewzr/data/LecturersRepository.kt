package pl.expert.mobilewzr.data

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import pl.expert.mobilewzr.data.model.Lecturer
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LecturersRepository"

@Singleton
class LecturersRepository @Inject constructor(
    private val firebaseFunctions: FirebaseFunctions
) {

    suspend fun getLecturers(): List<Lecturer> {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(URLs.LECTURERS_URL).get()
        }

        Log.i(TAG, "Lecturers downloaded")

        val lecturers: MutableList<Lecturer> = mutableListOf()

        val titles = doc.select(".pracownik_b")
        val descriptions = doc.select(".pracownik_t")
        val downloadedLecturers = titles.zip(descriptions)

        downloadedLecturers.forEach { (title, description) ->
            val cardRight = description.select(".wizytowka_info.prawa").eachText()

            val info = StringBuilder()
            description.select(".konsultacje div").forEach {
                info.append(it.text())
                info.append("\n")
            }

            lecturers.add(
                Lecturer(
                    name = title.text(),
                    email = description.select(".wizytowka_info.prawa a").text(),
                    phone = cardRight.getOrNull(1) ?: "",
                    room = cardRight.getOrNull(2) ?: "",
                    info = info.toString().trim()
                )
            )
        }

        return lecturers
    }

    suspend fun registerLecturer(email: String, password: String, token: String) {
        val data = hashMapOf(
            "email" to email,
            "password" to password,
            "token" to token
        )

        firebaseFunctions.getHttpsCallable("registerLecturer")
            .call(data)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    try {
                        throw Exception(task.exception?.message ?: "Exception message was null")
                    } catch (ex: Exception) {
                        Log.d(TAG, task.exception?.message ?: "Exception message was null")
                    }
                }
            }
            .await()
    }

}
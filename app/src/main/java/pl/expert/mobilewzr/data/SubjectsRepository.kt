package pl.expert.mobilewzr.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pl.expert.mobilewzr.data.db.SubjectsDao
import pl.expert.mobilewzr.data.model.Subject
import pl.expert.mobilewzr.domain.domainmodel.TimetableDataHolder
import pl.expert.mobilewzr.util.SubjectsUtils
import pl.expert.mobilewzr.util.TimetableViewUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SubjectsRepository"

@Singleton
class SubjectsRepository @Inject constructor(
    private val wzrService: WZRService,
    private val subjectsDao: SubjectsDao
) {

    suspend fun getSubjectsFromDb(): List<Subject> {
        return subjectsDao.getSubjects()
    }

    suspend fun updateSubject(subject: Subject) {
        subjectsDao.updateSubjects(subject)
    }

    suspend fun updateSubjects(subjects: List<Subject>) {
        subjectsDao.updateSubjects(*subjects.toTypedArray())
    }

    suspend fun addSubject(subject: Subject) {
        subjectsDao.addSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject) {
        subjectsDao.deleteSubjects(subject)
    }

    suspend fun replaceSubjectsInDb(subjects: List<Subject>) {
        subjectsDao.deleteSubjects()
        subjectsDao.addSubjects(subjects)
    }

    suspend fun getTimetableDataFromDb(): TimetableDataHolder {
        val subjects = subjectsDao.getSubjects()
        val timetableData = TimetableViewUtils.getDayViewDataHolderFrom(subjects)

        Log.i(TAG, "Subjects get from DB")

        return timetableData
    }

    suspend fun getFullTimetableDataFromService(groupId: String): TimetableDataHolder {
        val rawDownloadedSubjects = wzrService.getSubjects(groupId)
        val fixedSubjects = SubjectsUtils.fix(rawDownloadedSubjects)
        val mergedSubjects = SubjectsUtils.mergeMultipleSubjectsIntoOne(fixedSubjects)
        val timetableData = TimetableViewUtils.getDayViewDataHolderFrom(mergedSubjects)

        Log.i(TAG, "All subjects successfully downloaded")

        return timetableData
    }

    suspend fun getFirstTwoWeeksTimetableDataFromService(groupId: String): TimetableDataHolder {
        val rawDownloadedSubjects = wzrService.getSubjects(groupId)
        val firstTwoWeeksSubjects = SubjectsUtils.getOnlyFirstTwoWeeksSubjectsFrom(rawDownloadedSubjects)
        val fixedSubjects = SubjectsUtils.fix(firstTwoWeeksSubjects)
        val mergedSubjects = SubjectsUtils.mergeMultipleSubjectsIntoOne(fixedSubjects)
        val timetableData = TimetableViewUtils.getDayViewDataHolderFrom(mergedSubjects)

        Log.i(TAG, "Subjects successfully downloaded")

        return timetableData
    }

    suspend fun getGroups(): List<String> {
        var doc = Document("")
        try {
            doc = withContext(Dispatchers.IO) {
                Jsoup.connect(URLs.GROUPS_URL).get()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        Log.i(TAG, "Groups downloaded")

        val groups = doc.select("select option:not(:first-child)")
        return groups.eachText()
    }

}
package pl.expert.mobilewzr.data

import android.util.Log
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
    private val subjectsDao: SubjectsDao,
    private val groupsRepository: GroupsRepository
) {

    suspend fun getSubjectsFromDb(): List<Subject> {
        return subjectsDao.getSubjects()
    }

    suspend fun getSubjectsByLecturerFromDb(lecturer: String): List<Subject> {
        val parsedLecturerName = lecturer.substringBefore(",")
        return subjectsDao.getSubjectsByLecturer(parsedLecturerName)
    }

    suspend fun areLecturersSubjectsDownloaded(): Boolean {
        return subjectsDao.getLecturersSubjectsCount() != 0
    }

    suspend fun getTimetableDataFromDb(): TimetableDataHolder {
        val subjects = subjectsDao.getMyGroupSubjects()
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

    suspend fun getAllFirstTwoWeeksSubjectsFromService() {
        val allSubjects = mutableListOf<Subject>()

        subjectsDao.deleteLecturersSubjects()

        val groups = groupsRepository.getGroups().filter { g -> g.startsWith("S") }
        for (group in groups) {
            val rawDownloadedSubjects = wzrService.getSubjects(group)
            val firstTwoWeeksSubjects = SubjectsUtils.getOnlyFirstTwoWeeksSubjectsFrom(rawDownloadedSubjects)
            allSubjects.addAll(firstTwoWeeksSubjects)
        }

        val fixedSubjects = SubjectsUtils.fix(allSubjects)
        val mergedSubjects = SubjectsUtils.mergeMultipleSubjectsIntoOne(fixedSubjects)

        subjectsDao.addSubjects(mergedSubjects)

        Log.i(TAG, "Subjects successfully downloaded and inserted")
    }

    suspend fun addSubject(subject: Subject) {
        subject.isMyGroup = true
        subjectsDao.addSubject(subject)
    }

    suspend fun updateSubject(subject: Subject) {
        subjectsDao.updateSubjects(subject)
    }

    suspend fun updateSubjects(subjects: List<Subject>) {
        subjectsDao.updateSubjects(*subjects.toTypedArray())
    }

    suspend fun deleteSubject(subject: Subject) {
        subjectsDao.deleteSubjects(subject)
    }

    suspend fun replaceMyGroupSubjectsInDbWith(subjects: List<Subject>) {
        subjectsDao.deleteMyGroupSubjects()
        subjects.map { s -> s.isMyGroup = true }
        subjectsDao.addSubjects(subjects)
    }

}
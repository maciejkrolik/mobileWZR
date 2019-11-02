package pl.expert.mobilewzr.data.db

import androidx.room.*
import pl.expert.mobilewzr.data.model.Subject

@Dao
interface SubjectsDao {

    @Query("SELECT * FROM Subject")
    suspend fun getSubjects(): List<Subject>

    @Query("SELECT * FROM Subject WHERE isMyGroup = 1")
    suspend fun getMyGroupSubjects(): List<Subject>

    @Query("SELECT * FROM Subject WHERE description LIKE '%' || :lecturer || '%'")
    suspend fun getSubjectsByLecturer(lecturer: String): List<Subject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjects(subjects: List<Subject>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubject(subject: Subject)

    @Update
    suspend fun updateSubjects(vararg subjects: Subject)

    @Delete
    suspend fun deleteSubjects(vararg subjects: Subject)

    @Query("DELETE FROM Subject WHERE isMyGroup = 1")
    suspend fun deleteMyGroupSubjects()

    @Query("DELETE FROM Subject WHERE isMyGroup = 0")
    suspend fun deleteLecturersSubjects()

}

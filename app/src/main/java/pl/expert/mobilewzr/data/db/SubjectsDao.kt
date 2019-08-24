package pl.expert.mobilewzr.data.db

import androidx.room.*
import pl.expert.mobilewzr.data.model.Subject

@Dao
interface SubjectsDao {

    @Query("SELECT * FROM Subject")
    suspend fun getSubjects(): List<Subject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjects(subjects: List<Subject>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubject(subject: Subject)

    @Update
    suspend fun updateSubjects(vararg subjects: Subject)

    @Delete
    suspend fun deleteSubjects(vararg subjects: Subject)

    @Query("DELETE FROM Subject")
    suspend fun deleteSubjects()
}
package pl.expert.mobilewzr.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.expert.mobilewzr.data.model.Subject

@Dao
interface SubjectsDao {

    @Query("SELECT * FROM Subject")
    suspend fun getSubjects(): List<Subject>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjects(subjects: List<Subject>)

    @Query("DELETE FROM Subject")
    suspend fun deleteSubjects()
}

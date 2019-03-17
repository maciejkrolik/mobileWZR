package pl.expert.mobilewzr.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.univocity.parsers.annotations.Format
import com.univocity.parsers.annotations.Parsed
import java.util.*

@Entity
data class Subject(
    @PrimaryKey var index: Int = 0,

    @Parsed(index = 0, defaultNullRead = "") var title: String = "",

    @Parsed(index = 1) @Format(formats = ["MM/dd/yyyy"]) var startDate: Date = Date(),

    @Parsed(index = 2, defaultNullRead = "") var startTime: String = "",

    @Parsed(index = 3) @Format(formats = ["MM/dd/yyyy"]) var endDate: Date = Date(),

    @Parsed(index = 4, defaultNullRead = "") var endTime: String = "",

    @Parsed(index = 5, defaultNullRead = "") var description: String = "",

    @Parsed(index = 6, defaultNullRead = "") var location: String = ""
)

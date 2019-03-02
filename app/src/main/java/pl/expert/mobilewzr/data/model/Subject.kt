package pl.expert.mobilewzr.data.model

import com.univocity.parsers.annotations.Format
import com.univocity.parsers.annotations.Parsed

import java.util.Date

data class Subject(
    @Parsed(index = 0) val title: String = "",

    @Parsed(index = 1) @Format(formats = ["MM/dd/yyyy"]) val startDate: Date = Date(),

    @Parsed(index = 2) val startTime: String = "",

    @Parsed(index = 3) @Format(formats = ["MM/dd/yyyy"]) val endDate: Date = Date(),

    @Parsed(index = 4) val endTime: String = "",

    @Parsed(index = 5) val description: String = "",

    @Parsed(index = 6) val location: String = ""
)

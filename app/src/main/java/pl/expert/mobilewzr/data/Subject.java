package pl.expert.mobilewzr.data;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;

import java.util.Date;

public class Subject {

    @Parsed(index = 0)
    private String Title;

    @Format(formats = "dd/MM/yyyy")
    @Parsed(index = 1)
    private Date StartDate;

    @Parsed(index = 2)
    private String StartTime;

    @Format(formats = "dd/MM/yyyy")
    @Parsed(index = 3)
    private Date EndDate;

    @Parsed(index = 4)
    private String EndTime;

    @Parsed(index = 5)
    private String Description;

    @Parsed(index = 6)
    private String Location;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}

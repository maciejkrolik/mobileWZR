package pl.expert.mobilewzr.data;

import com.univocity.parsers.annotations.Parsed;

public class Subject {

    @Parsed(index = 0)
    private String Title;

    @Parsed(index = 1)
    private String StartDate;

    @Parsed(index = 2)
    private String StartTime;

    @Parsed(index = 3)
    private String EndDate;

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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
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

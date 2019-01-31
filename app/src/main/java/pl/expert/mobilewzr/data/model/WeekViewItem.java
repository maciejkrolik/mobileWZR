package pl.expert.mobilewzr.data.model;

import java.util.List;

public class WeekViewItem {

    private String Time;

    private List<String> ListOfSubjects;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public List<String> getListOfSubjects() {
        return ListOfSubjects;
    }

    public void setListOfSubjects(List<String> listOfSubjects) {
        ListOfSubjects = listOfSubjects;
    }
}

package com.mahavir_infotech.vidyasthali.models.GetTimeTable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("list_timetable")
    @Expose
    private List<ListTimetable> listTimetable = null;

    public List<ListTimetable> getListTimetable() {
        return listTimetable;
    }

    public void setListTimetable(List<ListTimetable> listTimetable) {
        this.listTimetable = listTimetable;
    }
}

package com.mahavir_infotech.vidyasthali.models.Result_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListResult {
    @SerializedName("timetable_id")
    @Expose
    private String timetableId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("attach_notes")
    @Expose
    private String attachNotes;

    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttachNotes() {
        return attachNotes;
    }

    public void setAttachNotes(String attachNotes) {
        this.attachNotes = attachNotes;
    }
}

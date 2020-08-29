package com.mahavir_infotech.vidyasthali.models.GetTimeTable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListTimetable implements Serializable {
    @SerializedName("timetable_id")
    @Expose
    private String timetableId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("attach_file")
    @Expose
    private String attachFile;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }
}

package com.mahavir_infotech.vidyasthali.models.List_home_Work;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListHomework implements Serializable {
    @SerializedName("homework_id")
    @Expose
    private String homeworkId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("homework_date")
    @Expose
    private String homeworkDate;
    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pick_image")
    @Expose
    private String pickImage;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHomeworkDate() {
        return homeworkDate;
    }

    public void setHomeworkDate(String homeworkDate) {
        this.homeworkDate = homeworkDate;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickImage() {
        return pickImage;
    }

    public void setPickImage(String pickImage) {
        this.pickImage = pickImage;
    }
}

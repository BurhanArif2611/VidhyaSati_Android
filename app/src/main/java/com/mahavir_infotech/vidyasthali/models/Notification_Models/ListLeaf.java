package com.mahavir_infotech.vidyasthali.models.Notification_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListLeaf implements Serializable {
    @SerializedName("notificatioin_id")
    @Expose
    private String notificatioinId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getNotificatioinId() {
        return notificatioinId;
    }

    public void setNotificatioinId(String notificatioinId) {
        this.notificatioinId = notificatioinId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

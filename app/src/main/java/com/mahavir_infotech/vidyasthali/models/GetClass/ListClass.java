package com.mahavir_infotech.vidyasthali.models.GetClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListClass {
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("class_name")
    @Expose
    private String className;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

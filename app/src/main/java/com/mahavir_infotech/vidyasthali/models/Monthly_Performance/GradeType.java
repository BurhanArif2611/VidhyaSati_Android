package com.mahavir_infotech.vidyasthali.models.Monthly_Performance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GradeType implements Serializable {
    @SerializedName("grade_id")
    @Expose
    private String gradeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("remark")
    @Expose
    private String remark;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

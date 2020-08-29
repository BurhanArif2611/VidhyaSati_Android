package com.mahavir_infotech.vidyasthali.models.Monthly_Performance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListStudent implements Serializable {
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("grade_types")
    @Expose
    private List<GradeType> gradeTypes = null;
    @SerializedName("description")
    @Expose
    private String description;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<GradeType> getGradeTypes() {
        return gradeTypes;
    }

    public void setGradeTypes(List<GradeType> gradeTypes) {
        this.gradeTypes = gradeTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

package com.mahavir_infotech.vidyasthali.models.Attendence_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListStudent {
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_present")
    @Expose
    private String is_present;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIs_present() {
        return is_present;
    }

    public void setIs_present(String is_present) {
        this.is_present = is_present;
    }
}

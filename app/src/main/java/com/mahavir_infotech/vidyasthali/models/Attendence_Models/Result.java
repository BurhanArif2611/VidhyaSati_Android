package com.mahavir_infotech.vidyasthali.models.Attendence_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("list_student")
    @Expose
    private List<ListStudent> listStudent = null;

    public List<ListStudent> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<ListStudent> listStudent) {
        this.listStudent = listStudent;
    }
}

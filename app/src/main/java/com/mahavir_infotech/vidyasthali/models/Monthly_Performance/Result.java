package com.mahavir_infotech.vidyasthali.models.Monthly_Performance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
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

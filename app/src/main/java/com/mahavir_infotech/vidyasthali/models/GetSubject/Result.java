package com.mahavir_infotech.vidyasthali.models.GetSubject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("list_subjects")
    @Expose
    private List<ListSubject> listSubjects = null;

    public List<ListSubject> getListSubjects() {
        return listSubjects;
    }

    public void setListSubjects(List<ListSubject> listSubjects) {
        this.listSubjects = listSubjects;
    }
}

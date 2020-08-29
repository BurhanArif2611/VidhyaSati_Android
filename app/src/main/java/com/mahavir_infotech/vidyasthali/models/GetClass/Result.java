package com.mahavir_infotech.vidyasthali.models.GetClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("list_classes")
    @Expose
    private List<ListClass> listClasses = null;

    public List<ListClass> getListClasses() {
        return listClasses;
    }

    public void setListClasses(List<ListClass> listClasses) {
        this.listClasses = listClasses;
    }
}

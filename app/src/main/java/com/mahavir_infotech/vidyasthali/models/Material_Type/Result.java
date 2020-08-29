package com.mahavir_infotech.vidyasthali.models.Material_Type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("list_types")
    @Expose
    private List<ListType> listTypes = null;

    public List<ListType> getListTypes() {
        return listTypes;
    }

    public void setListTypes(List<ListType> listTypes) {
        this.listTypes = listTypes;
    }
}

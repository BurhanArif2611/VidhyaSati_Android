package com.mahavir_infotech.vidyasthali.models.LeaveList_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("list_leaves")
    @Expose
    private List<ListLeaf> listLeaves = null;

    public List<ListLeaf> getListLeaves() {
        return listLeaves;
    }

    public void setListLeaves(List<ListLeaf> listLeaves) {
        this.listLeaves = listLeaves;
    }
}

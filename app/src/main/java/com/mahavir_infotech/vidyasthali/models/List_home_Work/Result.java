package com.mahavir_infotech.vidyasthali.models.List_home_Work;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    @SerializedName("list_homework")
    @Expose
    private List<ListHomework> listHomework = null;

    public List<ListHomework> getListHomework() {
        return listHomework;
    }

    public void setListHomework(List<ListHomework> listHomework) {
        this.listHomework = listHomework;
    }
}

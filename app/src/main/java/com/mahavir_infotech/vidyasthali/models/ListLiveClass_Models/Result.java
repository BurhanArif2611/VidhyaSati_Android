package com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result  implements Serializable {
    @SerializedName("list_session")
    @Expose
    private List<ListSession> listSession = null;

    public List<ListSession> getListSession() {
        return listSession;
    }

    public void setListSession(List<ListSession> listSession) {
        this.listSession = listSession;
    }
}

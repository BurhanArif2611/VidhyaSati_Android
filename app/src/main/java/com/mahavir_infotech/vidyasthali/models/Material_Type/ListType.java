package com.mahavir_infotech.vidyasthali.models.Material_Type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListType {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("material_type")
    @Expose
    private String materialType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
}

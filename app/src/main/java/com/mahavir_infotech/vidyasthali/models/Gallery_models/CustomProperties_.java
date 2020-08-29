package com.mahavir_infotech.vidyasthali.models.Gallery_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomProperties_ {
    @SerializedName("generated_conversions")
    @Expose
    private GeneratedConversions_ generatedConversions;

    public GeneratedConversions_ getGeneratedConversions() {
        return generatedConversions;
    }

    public void setGeneratedConversions(GeneratedConversions_ generatedConversions) {
        this.generatedConversions = generatedConversions;
    }
}

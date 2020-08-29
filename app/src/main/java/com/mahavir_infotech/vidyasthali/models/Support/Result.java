package com.mahavir_infotech.vidyasthali.models.Support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable {
    @SerializedName("footer_mobile")
    @Expose
    private String footerMobile;
    @SerializedName("visitor_count")
    @Expose
    private String visitorCount;
    @SerializedName("footer_facebook")
    @Expose
    private String footerFacebook;
    @SerializedName("footer_address")
    @Expose
    private String footerAddress;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;
    @SerializedName("contact_phone")
    @Expose
    private String contactPhone;
    @SerializedName("contact_address")
    @Expose
    private String contactAddress;

    public String getFooterMobile() {
        return footerMobile;
    }

    public void setFooterMobile(String footerMobile) {
        this.footerMobile = footerMobile;
    }

    public String getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(String visitorCount) {
        this.visitorCount = visitorCount;
    }

    public String getFooterFacebook() {
        return footerFacebook;
    }

    public void setFooterFacebook(String footerFacebook) {
        this.footerFacebook = footerFacebook;
    }

    public String getFooterAddress() {
        return footerAddress;
    }

    public void setFooterAddress(String footerAddress) {
        this.footerAddress = footerAddress;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

}

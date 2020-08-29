package com.mahavir_infotech.vidyasthali.models.GetSyllabus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListHomework implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("material_id")
    @Expose
    private String materialId;
    @SerializedName("subject_id")
    @Expose
    private String subject_id;
    @SerializedName("class_id")
    @Expose
    private String class_id;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("link_type")
    @Expose
    private String linkType;
    @SerializedName("attach_link")
    @Expose
    private String attachLink;
    @SerializedName("attach_notes")
    @Expose
    private String attach_notes;
    @SerializedName("attach_paper")
    @Expose
    private String attach_paper;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getMaterialId() {
        return materialId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachLink() {
        return attachLink;
    }

    public void setAttachLink(String attachLink) {
        this.attachLink = attachLink;
    }

    public String getAttach_notes() {
        return attach_notes;
    }

    public void setAttach_notes(String attach_notes) {
        this.attach_notes = attach_notes;
    }

    public String getAttach_paper() {
        return attach_paper;
    }

    public void setAttach_paper(String attach_paper) {
        this.attach_paper = attach_paper;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
}

package com.mahavir_infotech.vidyasthali.models;

public class RecentModel {
    String user_id;
    String username;
    String email;
    String User_profile_pic;
    String last_message,unread_msg;
    String role;
    String class_name;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUnread_msg() {
        return unread_msg;
    }

    public void setUnread_msg(String unread_msg) {
        this.unread_msg = unread_msg;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    String created_at;

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_profile_pic() {
        return User_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        User_profile_pic = user_profile_pic;
    }
}

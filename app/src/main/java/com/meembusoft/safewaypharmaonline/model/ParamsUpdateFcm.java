package com.meembusoft.safewaypharmaonline.model;


public class ParamsUpdateFcm {
    private String user_type = "";
    private String user_id = "";
    private String fcm_token = "";


    public ParamsUpdateFcm(String user_type, String user_id, String fcm_token) {
        this.user_type = user_type;
        this.user_id = user_id;
        this.fcm_token = fcm_token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    @Override
    public String toString() {
        return "{" +
                "user_type='" + user_type + '\'' +
                ", user_id='" + user_id + '\'' +
                ", fcm_token='" + fcm_token + '\'' +
                '}';
    }
}

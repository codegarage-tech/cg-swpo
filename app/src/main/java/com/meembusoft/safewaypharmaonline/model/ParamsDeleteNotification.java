package com.meembusoft.safewaypharmaonline.model;


public class ParamsDeleteNotification {
    private String id = "";

    public ParamsDeleteNotification(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ParamsDeleteNotification{" +
                "id='" + id + '\'' +
                '}';
    }
}

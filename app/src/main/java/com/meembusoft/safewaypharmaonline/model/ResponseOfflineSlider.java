package com.meembusoft.safewaypharmaonline.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseOfflineSlider {

    private String status = "";
    private String message = "";
    private List<Slider> data = new ArrayList<>();

    public ResponseOfflineSlider() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Slider> getData() {
        return data;
    }

    public void setData(List<Slider> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseOfflineSlider{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class FilterSessionLeft {
    private long time;
    private List<String> images = null;
    private List<String> descriptions = null;
    private List<String> id = null;
    private String  formattedTime = "";
    private String  status = "";

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "time=" + time +
                ", images=" + images +
                ", descriptions=" + descriptions +
                ", id=" + id +
                ", formattedTime=" + formattedTime +
                ", status=" + status +
                '}';
    }
}

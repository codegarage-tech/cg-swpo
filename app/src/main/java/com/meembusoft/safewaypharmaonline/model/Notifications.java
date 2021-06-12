package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class Notifications {
    private String id = "";
    private String notification_type = "";
    private String title = "";
    private String message = "";
    private String status = "";
    private String created_at = "";
    private String description = "";
    private String notify_time = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id='" + id + '\'' +
                ", notification_type='" + notification_type + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", description='" + description + '\'' +
                ", notify_time='" + notify_time + '\'' +
                '}';
    }
}

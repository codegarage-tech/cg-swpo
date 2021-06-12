package com.meembusoft.safewaypharmaonline.model;


public class ParamsUpdateStatusReminder {
    private String customer_id = "";
    private String date = "";
    private String time = "";
    private String status = "";

    public ParamsUpdateStatusReminder(String customer_id, String date, String time, String status) {
        this.customer_id = customer_id;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ParamsUpdateReminderStatus{" +
                "customer_id='" + customer_id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

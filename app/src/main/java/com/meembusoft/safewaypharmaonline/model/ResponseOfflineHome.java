package com.meembusoft.safewaypharmaonline.model;

public class ResponseOfflineHome {
    private String status = "";
    private String msg = "";
    private Home data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Home getData() {
        return data;
    }

    public void setData(Home data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

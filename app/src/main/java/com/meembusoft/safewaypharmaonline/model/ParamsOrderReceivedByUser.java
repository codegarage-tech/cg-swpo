package com.meembusoft.safewaypharmaonline.model;

public class ParamsOrderReceivedByUser {
    private String order_id = "";
    private String review_rating = "";

    public ParamsOrderReceivedByUser() {
    }

    public ParamsOrderReceivedByUser(String order_id, String review_rating) {
        this.order_id = order_id;
        this.review_rating = review_rating;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    @Override
    public String toString() {
        return "{" +
                "order_id='" + order_id + '\'' +
                ", review_rating='" + review_rating + '\'' +
                '}';
    }
}

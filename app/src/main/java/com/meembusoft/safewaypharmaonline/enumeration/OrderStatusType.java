package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum OrderStatusType {
    DONE("1"),
    PENDING("2"),
    CANCELLED("3"),
    ACCEPTED("4"),
    DELIVERED_TO_DELIVERY_MAN("5"),
    RECEIVED_BY_USER("6");

    private final String paymentStatusValue;

    private OrderStatusType(String value) {
        paymentStatusValue = value;
    }

    public static String getMessage(String orderStatusValue){
        String message = "";
        switch (getPaymentStatus(orderStatusValue)){
            case DONE:
                message = "Completed";
                break;
            case PENDING:
                message = "Pending";
                break;
            case CANCELLED:
                message = "Cancelled";
                break;
            case ACCEPTED:
                message = "Accepted";
                break;
            case DELIVERED_TO_DELIVERY_MAN:
                message = "Delivery on the way";
                break;
            case RECEIVED_BY_USER:
                message = "Received by user";
                break;
        }
        return message;
    }

    public boolean equalsName(String otherName) {
        return paymentStatusValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.paymentStatusValue;
    }

    public static OrderStatusType getPaymentStatus(String value) {
        for (OrderStatusType paymentStatus : OrderStatusType.values()) {
            if (paymentStatus.toString().equalsIgnoreCase(value)) {
                return paymentStatus;
            }
        }
        return null;
    }
}
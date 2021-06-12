package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum OrderNotificationType {

    NONE("")
    , DELIVERY_ON_THE_WAY("delivery_on_the_way")
    , PAYMENT_RECEIPT ("payment_receipt")
    , ACCEPT_ORDER("accept_order")
    , NEW_ORDER("new_order")
    , PROMO_NOTIFICATION("promo_notification")
    , APP_NOTIFICATION("app_notification");
    private final String notificationTypeValue;

    private OrderNotificationType(String value) {
        notificationTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return notificationTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.notificationTypeValue;
    }

    public static OrderNotificationType getNotificationType(String value) {
        for (OrderNotificationType notificationType : OrderNotificationType.values()) {
            if (notificationType.toString().equalsIgnoreCase(value)) {
                return notificationType;
            }
        }
        return null;
    }
}
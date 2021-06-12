package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum OrderCheckoutType {

    NONE("")
    , CART_TO_CHECKOUT("Cart To checkout")
    , BUY_TO_CHECKOUT("Buy To checkout");

    private final String orderCheckoutValue;

    private OrderCheckoutType(String value) {
        orderCheckoutValue = value;
    }

    public boolean equalsName(String otherName) {
        return orderCheckoutValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.orderCheckoutValue;
    }

    public static OrderCheckoutType getOrderCheckoutType(String value) {
        for (OrderCheckoutType mOrderCheckoutValue : OrderCheckoutType.values()) {
            if (mOrderCheckoutValue.toString().equalsIgnoreCase(value)) {
                return mOrderCheckoutValue;
            }
        }
        return null;
    }
}
package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum OrderActionType {

    NONE("")
    , SKIP("Skip")
    , ACCEPT("Accept")
    , STATUS("Status");
    private final String storeTypeValue;

    private OrderActionType(String value) {
        storeTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return storeTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.storeTypeValue;
    }

    public static OrderActionType getStoreype(String value) {
        for (OrderActionType storeType : OrderActionType.values()) {
            if (storeType.toString().equalsIgnoreCase(value)) {
                return storeType;
            }
        }
        return null;
    }
}
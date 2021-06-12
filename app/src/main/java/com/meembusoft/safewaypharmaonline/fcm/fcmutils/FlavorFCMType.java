package com.meembusoft.safewaypharmaonline.fcm.fcmutils;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum FlavorFCMType {

    CUSTOMER("customer"),
    SUPPLIER("supplier");

    private final String flavorType;

    private FlavorFCMType(String value) {
        flavorType = value;
    }

    public boolean equalsName(String otherName) {
        return flavorType.equals(otherName);
    }

    public String toString() {
        return this.flavorType;
    }
}
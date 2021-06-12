package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum FlavorType {

    CUSTOMER("customer"),
    SUPPLIER("supplier");

    private final String flavorType;

    private FlavorType(String value) {
        flavorType = value;
    }

    public boolean equalsName(String otherName) {
        return flavorType.equals(otherName);
    }

    public String toString() {
        return this.flavorType;
    }

    public static FlavorType getFlavor(String value) {
        for (FlavorType flavorType : FlavorType.values()) {
            if (flavorType.toString().equalsIgnoreCase(value)) {
                return flavorType;
            }
        }
        return null;
    }
}
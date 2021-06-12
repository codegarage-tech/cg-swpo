package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum ShippingType {

    NONE("")
    , ADD_TO_SHIPPING("Add To Shipping")
    , EDIT_TO_SHIPPING("Edit To Shipping");

    private final String shippingTypeValue;

    private ShippingType(String value) {
        shippingTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return shippingTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.shippingTypeValue;
    }

    public static ShippingType getShippingType(String value) {
        for (ShippingType mShippingType : ShippingType.values()) {
            if (mShippingType.toString().equalsIgnoreCase(value)) {
                return mShippingType;
            }
        }
        return null;
    }
}
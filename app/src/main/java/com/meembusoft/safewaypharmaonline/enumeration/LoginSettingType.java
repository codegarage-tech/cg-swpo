package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum LoginSettingType {

    NONE("")
    , HOME_TO_LOGIN("Home To Login")
    , MENU_TO_LOGIN("Menu To Login")
    , DETAILS_TO_LOGIN("Details To Login")
    , ORDER_CART_TO_LOGIN("Order Cart To Login")
    , OTHERS("Others To Login");
    private final String loginyTypeValue;

    private LoginSettingType(String value) {
        loginyTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return loginyTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.loginyTypeValue;
    }

    public static LoginSettingType getLoginSettingType(String value) {
        for (LoginSettingType loginSettingType : LoginSettingType.values()) {
            if (loginSettingType.toString().equalsIgnoreCase(value)) {
                return loginSettingType;
            }
        }
        return null;
    }
}
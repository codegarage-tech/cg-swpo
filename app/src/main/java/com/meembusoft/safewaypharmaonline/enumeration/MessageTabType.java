package com.meembusoft.safewaypharmaonline.enumeration;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum MessageTabType {

    ORDERS("orders"),
    CHATS("chats"),
    PROMOS("promos"),
    SETTINGS("settings");

    private final String messageTabType;

    private MessageTabType(String value) {
        messageTabType = value;
    }

    public boolean equalsName(String otherName) {
        return messageTabType.equals(otherName);
    }

    public String toString() {
        return this.messageTabType;
    }

    public static MessageTabType getFlavor(String value) {
        for (MessageTabType messageTabType : MessageTabType.values()) {
            if (messageTabType.toString().equalsIgnoreCase(value)) {
                return messageTabType;
            }
        }
        return null;
    }
}
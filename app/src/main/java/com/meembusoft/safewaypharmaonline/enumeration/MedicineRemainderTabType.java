package com.meembusoft.safewaypharmaonline.enumeration;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum MedicineRemainderTabType {

    ADD_REMAINDER("add remainder"),
    PILLBOX("pillbox"),
    SESSION("session");

    private final String medicineRemainderTabType;

    private MedicineRemainderTabType(String value) {
        medicineRemainderTabType = value;
    }

    public boolean equalsName(String otherName) {
        return medicineRemainderTabType.equals(otherName);
    }

    public String toString() {
        return this.medicineRemainderTabType;
    }

    public static MedicineRemainderTabType getFlavor(String value) {
        for (MedicineRemainderTabType medicineRemainderTabType : MedicineRemainderTabType.values()) {
            if (medicineRemainderTabType.toString().equalsIgnoreCase(value)) {
                return medicineRemainderTabType;
            }
        }
        return null;
    }
}
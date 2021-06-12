package com.meembusoft.safewaypharmaonline.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum CategoryType {

    NONE("")
    , CATEGORY("Category")
    , BASE_SELLER("Base Seller")
    , GENERAL_MEDICINE("General RelatedMedicine")
    , MEDICAL_INSTRUMENT("Medical Instrument")
    , HERBAL_MEDICINE("Herbal RelatedMedicine")
    , COSMETICS("Cosmetics")
    , BABY_FOOD_STATIONARY("Baby Food")
    , KIDS_FOOD("Kids Food")
    , MEDICAL_SUPPORT("Medical Support")
    , OPTICS_LENS("Optics Lens")
    , SHOP("Shop");
    private final String categoryTypeValue;

    private CategoryType(String value) {
        categoryTypeValue = value;
    }

    public boolean equalsName(String otherName) {
        return categoryTypeValue.equals(otherName);
    }

    @Override
    public String toString() {
        return this.categoryTypeValue;
    }

    public static CategoryType getKitchenType(String value) {
        for (CategoryType kitchenType : CategoryType.values()) {
            if (kitchenType.toString().equalsIgnoreCase(value)) {
                return kitchenType;
            }
        }
        return null;
    }
}
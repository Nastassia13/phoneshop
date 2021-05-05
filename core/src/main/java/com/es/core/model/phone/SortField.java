package com.es.core.model.phone;

public enum SortField {
    ID("id"),
    BRAND("brand"),
    MODEL("model"),
    DISPLAY_SIZE("displaySizeInches"),
    PRICE("price");
    private String string;

    SortField(String string) {
        this.string = string;
    }

    public static SortField getSortFieldByString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (SortField field : values()) {
            if (field.toString().equals(str)) {
                return field;
            }
        }
        throw new IllegalArgumentException("No enum found with string: " + str);
    }

    @Override
    public String toString() {
        return string;
    }
}

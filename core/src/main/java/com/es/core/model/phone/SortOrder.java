package com.es.core.model.phone;

public enum SortOrder {
    ASC("asc"), DESC("desc");
    private String string;

    SortOrder(String string) {
        this.string = string;
    }

    public static SortOrder getSortOrderByString(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        for (SortOrder order : values()) {
            if (order.toString().equals(str)) {
                return order;
            }
        }
        throw new IllegalArgumentException("No enum found with string: " + str);
    }

    @Override
    public String toString() {
        return string;
    }
}

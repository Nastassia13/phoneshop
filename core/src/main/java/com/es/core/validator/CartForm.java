package com.es.core.validator;

import java.util.List;

public class CartForm {
    private List<String> phoneIds;
    private List<String> quantities;

    public List<String> getPhoneIds() {
        return phoneIds;
    }

    public void setPhoneIds(List<String> phoneIds) {
        this.phoneIds = phoneIds;
    }

    public List<String> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<String> quantities) {
        this.quantities = quantities;
    }
}

package com.es.core.model.cart;

public class CartItem {
    private Long phoneId;
    private long quantity;

    public CartItem(Long phoneId, long quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

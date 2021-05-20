package com.es.core.exception;

public class OrderNotFoundException extends RuntimeException {
    private String orderSecureId;
    private Long orderId;

    public OrderNotFoundException(String orderSecureId) {
        this.orderSecureId = orderSecureId;
    }

    public OrderNotFoundException(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSecureId() {
        return orderSecureId;
    }

    public void setOrderSecureId(String orderSecureId) {
        this.orderSecureId = orderSecureId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}

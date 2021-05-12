package com.es.core.exception;

public class PhoneNotFoundException extends RuntimeException {
    private Long phoneId;

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public PhoneNotFoundException(Long phoneId) {
        this.phoneId = phoneId;
    }
}

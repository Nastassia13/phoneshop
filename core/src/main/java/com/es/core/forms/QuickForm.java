package com.es.core.forms;

import com.es.core.constraints.ModelConstraint;
import com.es.core.constraints.StockConstraint;

import javax.validation.constraints.Min;

@StockConstraint
public class QuickForm {
    @ModelConstraint
    private String phoneModel;
    @Min(value = 1L, message = "Enter number more than 0.")
    private Long quantity;

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}

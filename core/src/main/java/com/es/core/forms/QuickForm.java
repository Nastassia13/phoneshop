package com.es.core.forms;

import java.util.List;

public class QuickForm {
    private List<String> phoneModels;
    private List<String> quantities;

    public List<String> getPhoneModels() {
        return phoneModels;
    }

    public void setPhoneModels(List<String> phoneModels) {
        this.phoneModels = phoneModels;
    }

    public List<String> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<String> quantities) {
        this.quantities = quantities;
    }
}

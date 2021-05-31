package com.es.core.forms;

import javax.validation.Valid;
import java.util.List;

public class QuickFormEntity {
    @Valid
    private List<QuickForm> quickForms;

    public List<QuickForm> getQuickForms() {
        return quickForms;
    }

    public void setQuickForms(List<QuickForm> quickForms) {
        this.quickForms = quickForms;
    }
}

package com.es.phoneshop.web.controller.pages;

import com.es.core.forms.QuickForm;
import com.es.core.forms.QuickFormEntity;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/quickOrder")
public class QuickOrderPageController {
    @Resource
    private CartService cartService;
    @Resource
    private PhoneService phoneService;

    @GetMapping
    public String getQuickOrder(@ModelAttribute QuickFormEntity quickFormEntity, Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "quickOrder";
    }

    @PutMapping
    public String add2Cart(@Valid @ModelAttribute QuickFormEntity quickFormEntity, BindingResult bindingResult, Model model) {
        List<String> addedSuccess = new ArrayList<>();
        for (int i = 0; i < quickFormEntity.getQuickForms().size(); i++) {
            checkErrors(quickFormEntity, bindingResult, addedSuccess, i);
        }
        model.addAttribute("success", addedSuccess);
        model.addAttribute("cart", cartService.getCart());
        return "quickOrder";
    }

    private void checkErrors(QuickFormEntity quickFormEntity, BindingResult bindingResult, List<String> addedSuccess, int i) {
        if (checkItem(quickFormEntity.getQuickForms().get(i), bindingResult, i)) {
            addedSuccess.add(quickFormEntity.getQuickForms().get(i).getPhoneModel());
            Phone phone = phoneService.getPhone(quickFormEntity.getQuickForms().get(i).getPhoneModel()).get();
            cartService.addPhone(new CartItem(phone, quickFormEntity.getQuickForms().get(i).getQuantity()));
            quickFormEntity.getQuickForms().set(i, new QuickForm());
        }
    }

    private boolean checkItem(QuickForm quickForm, BindingResult bindingResult, int i) {
        return !bindingResult.hasFieldErrors("quickForms[" + i + "]")
                && !bindingResult.hasFieldErrors("quickForms[" + i + "].phoneModel")
                && !bindingResult.hasFieldErrors("quickForms[" + i + "].quantity")
                && !quickForm.getPhoneModel().isEmpty();
    }
}

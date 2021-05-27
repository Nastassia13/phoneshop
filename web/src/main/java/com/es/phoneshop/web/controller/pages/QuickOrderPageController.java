package com.es.phoneshop.web.controller.pages;

import com.es.core.forms.QuickForm;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.validator.QuickFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
    @Resource
    private QuickFormValidator validator;

    @InitBinder("quickForm")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String getQuickOrder(Model model) {
        model.addAttribute("cart", cartService.getCart());
        if (!model.containsAttribute("quickForm")) {
            model.addAttribute("quickForm", new QuickForm());
        }
        return "quickOrder";
    }

    @PutMapping
    public String add2Cart(@Valid QuickForm quickForm, BindingResult bindingResult, Model model) {
        List<String> addedSuccess = new ArrayList<>();
        for (int i = 0; i < quickForm.getPhoneModels().size(); i++) {
            checkErrors(quickForm, bindingResult, addedSuccess, i);
        }
        model.addAttribute("success", addedSuccess);
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("quickForm", quickForm);
        return "quickOrder";
    }

    private void checkErrors(QuickForm quickForm, BindingResult bindingResult, List<String> addedSuccess, int i) {
        if (checkItem(quickForm, bindingResult, i)) {
            addedSuccess.add(quickForm.getPhoneModels().get(i));
            Phone phone = phoneService.getPhone(quickForm.getPhoneModels().get(i)).get();
            cartService.addPhone(new CartItem(phone, Long.parseLong(quickForm.getQuantities().get(i))));
            quickForm.getPhoneModels().set(i, null);
            quickForm.getQuantities().set(i, null);
        }
    }

    private boolean checkItem(QuickForm quickForm, BindingResult bindingResult, int i) {
        return !bindingResult.hasFieldErrors("phoneModels[" + i + "]")
                && !bindingResult.hasFieldErrors("quantities[" + i + "]")
                && !quickForm.getPhoneModels().get(i).isEmpty();
    }
}

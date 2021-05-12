package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails/{productId}")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductInfo(@PathVariable Long productId, Model model) {
        Optional<Phone> optionalPhone = phoneService.getPhone(productId);
        if (!optionalPhone.isPresent()) {
            throw new PhoneNotFoundException(productId);
        }
        model.addAttribute("phone", optionalPhone.get());
        model.addAttribute("cart", cartService.getCart());
        return "product";
    }
}

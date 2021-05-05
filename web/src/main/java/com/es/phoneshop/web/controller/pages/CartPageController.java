package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateCart() {
        cartService.update(null);
    }
}

package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.service.CartService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;

@ControllerAdvice
public class ErrorHandlerController {
    @Resource
    private CartService cartService;

    @ExceptionHandler(PhoneNotFoundException.class)
    public String productNotFoundPage(PhoneNotFoundException e, Model model) {
        model.addAttribute("id", e.getPhoneId());
        model.addAttribute("cart", cartService.getCart());
        return "productNotFound";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String orderNotFoundPage(OrderNotFoundException e, Model model) {
        model.addAttribute("id", e.getOrderId() != null ? e.getOrderId() : e.getOrderSecureId());
        model.addAttribute("cart", cartService.getCart());
        return "orderNotFound";
    }
}

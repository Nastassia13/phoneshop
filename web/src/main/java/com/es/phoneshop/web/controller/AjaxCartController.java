package com.es.phoneshop.web.controller;

import com.es.core.model.cart.CartItem;
import com.es.core.service.CartService;
import com.es.core.validator.CartItemForm;
import com.es.core.validator.CartItemValidator;
import com.es.phoneshop.web.controller.dto.AddingCartDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;
    @Resource
    private CartItemValidator validator;
    private static final String ADDED_TO_CART = "Added to cart successfully";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AddingCartDto addPhone(@Valid CartItemForm cartItemForm, BindingResult bindingResult) {
        AddingCartDto dto = new AddingCartDto();
        if (bindingResult.hasErrors()) {
            dto.setMessage(bindingResult.getAllErrors().get(0).getCode());
        } else {
            CartItem cartItem = new CartItem(Long.parseLong(cartItemForm.getPhoneId()), Long.parseLong(cartItemForm.getQuantity()));
            cartService.addPhone(cartItem);
            setDtoSuccess(dto);
        }
        return dto;
    }

    private void setDtoSuccess(AddingCartDto dto) {
        dto.setTotalCost(cartService.getCart().getTotalCost());
        dto.setTotalQuantity(cartService.getCart().getTotalQuantity());
        dto.setMessage(ADDED_TO_CART);
        dto.setSuccess(true);
    }
}

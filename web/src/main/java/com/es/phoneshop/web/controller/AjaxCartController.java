package com.es.phoneshop.web.controller;

import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.forms.CartItemForm;
import com.es.core.validator.CartItemFormValidator;
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
    private PhoneService phoneService;
    @Resource
    private CartItemFormValidator validator;
    private static final String ADDED_TO_CART = "Added to cart successfully";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AddingCartDto addPhone(@Valid CartItemForm cartItemForm, BindingResult bindingResult) {
        AddingCartDto dto = new AddingCartDto();
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> stringBuilder.append(error.getDefaultMessage()));
            dto.setMessage(stringBuilder.toString());
        } else {
            Phone phone = phoneService.getPhone(Long.parseLong(cartItemForm.getPhoneId())).get();
            CartItem cartItem = new CartItem(phone, Long.parseLong(cartItemForm.getQuantity()));
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

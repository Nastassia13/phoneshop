package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.validator.CartForm;
import com.es.core.validator.CartFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartFormValidator validator;

    @InitBinder("cartForm")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        model.addAttribute("cartForm", convertCart(cart));
        return "cart";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateCart(@Valid @ModelAttribute CartForm cartForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            cartService.update(convertCartForm(cartForm));
        }
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("cartForm", cartForm);
        return "cart";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteCartItem(@RequestParam Long phoneId, Model model) {
        cartService.remove(phoneId);
        model.addAttribute("cart", cartService.getCart());
        return "redirect:cart";
    }

    private CartForm convertCart(Cart cart) {
        CartForm cartForm = new CartForm();
        cartForm.setPhoneIds(cart.getItems().stream().map(CartItem::getPhone)
                .map(Phone::getId)
                .map(String::valueOf)
                .collect(Collectors.toList()));
        cartForm.setQuantities(cart.getItems().stream().map(CartItem::getQuantity)
                .map(String::valueOf)
                .collect(Collectors.toList()));
        return cartForm;
    }

    private List<CartItem> convertCartForm(CartForm cartForm) {
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < cartForm.getPhoneIds().size(); i++) {
            Phone phone = phoneService.getPhone(Long.parseLong(cartForm.getPhoneIds().get(i))).get();
            CartItem cartItem = new CartItem(phone, Long.parseLong(cartForm.getQuantities().get(i)));
            cartItems.add(cartItem);
        }
        return cartItems;
    }
}

package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.forms.OrderForm;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import com.es.core.validator.OrderFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderFormValidator validator;

    @InitBinder("orderForm")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model) {
        Order order = orderService.createOrder(cartService.getCart());
        model.addAttribute("order", order);
        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", new OrderForm());
        }
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@Valid OrderForm orderForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderService.createOrder(cartService.getCart());
        redirectAttributes.addFlashAttribute("orderForm", orderForm);
        if (!checkStock(order, redirectAttributes) || !checkCart(redirectAttributes)) {
            return "redirect:order";
        }
        if (bindingResult.hasErrors()) {
            return "order";
        }
        setOrderFields(order, orderForm);
        orderService.placeOrder(order);
        return "redirect:/orderOverview/" + order.getSecureId();
    }

    private boolean checkStock(Order order, RedirectAttributes redirectAttributes) {
        boolean check = true;
        try {
            orderService.checkStock(order);
        } catch (OutOfStockException e) {
            cartService.remove(e.getPhone().getId());
            redirectAttributes.addFlashAttribute("stockError", "Out of stock! Product has been removed.");
            check = false;
        }
        return check;
    }

    private boolean checkCart(RedirectAttributes redirectAttributes) {
        boolean check = true;
        if (cartService.getCart().getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyOrder", "Cart is empty! Add product to cart.");
            check = false;
        }
        return check;
    }

    private void setOrderFields(Order order, OrderForm orderForm) {
        order.setFirstName(orderForm.getFirstName());
        order.setLastName(orderForm.getLastName());
        order.setDeliveryAddress(orderForm.getDeliveryAddress());
        order.setContactPhoneNo(orderForm.getContactPhoneNo());
        if (orderForm.getAdditionalInformation() != null) {
            order.setAdditionalInformation(orderForm.getAdditionalInformation());
        }
        order.setStatus(OrderStatus.NEW);
    }
}

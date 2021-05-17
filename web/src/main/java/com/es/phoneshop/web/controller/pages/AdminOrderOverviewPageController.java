package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/admin/orders/{orderId}")
public class AdminOrderOverviewPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String showOrder(@PathVariable Long orderId, Model model) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException(orderId);
        }
        model.addAttribute("order", optionalOrder.get());
        return "adminOrderOverview";
    }
}

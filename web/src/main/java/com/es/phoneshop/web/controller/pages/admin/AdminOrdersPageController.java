package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.OrderStatus;
import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class AdminOrdersPageController {
    @Resource
    private OrderService orderService;

    @GetMapping(value = "/admin/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "adminOrders";
    }

    @GetMapping("/admin/orders/{orderId}")
    public String showOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", orderService.getOrderById(orderId));
        return "adminOrderOverview";
    }

    @PostMapping(value = "/admin/orders/{orderId}")
    public String setStatusDelivered(@PathVariable Long orderId, @RequestParam String status, Model model) {
        orderService.setStatus(orderId, OrderStatus.valueOf(status));
        model.addAttribute("order", orderService.getOrderById(orderId));
        return "adminOrderOverview";
    }
}

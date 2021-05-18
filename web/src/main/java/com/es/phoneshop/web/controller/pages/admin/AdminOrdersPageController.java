package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class AdminOrdersPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "adminOrders";
    }
}

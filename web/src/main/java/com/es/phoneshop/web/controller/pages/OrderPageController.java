package com.es.phoneshop.web.controller.pages;

import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public void getOrder() {
        orderService.createOrder(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void placeOrder() {
        orderService.placeOrder(null);
    }
}

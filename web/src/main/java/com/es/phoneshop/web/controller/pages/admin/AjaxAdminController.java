package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.PhoneService;
import com.es.core.service.impl.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class AjaxAdminController {
    @Resource
    private OrderServiceImpl orderService;
    @Resource
    private PhoneService phoneService;
    private static final String ORDER_DELIVERED = "Order delivered";
    private static final String ORDER_REJECTED = "Order rejected";

    @RequestMapping(method = RequestMethod.POST, value = "/ajaxAdminDelivered/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String setStatusDelivered(@PathVariable String orderId) {
        orderService.setStatus(Long.parseLong(orderId), OrderStatus.DELIVERED);
        return ORDER_DELIVERED;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/ajaxAdminRejected/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String setStatusRejected(@PathVariable String orderId) {
        orderService.setStatus(Long.parseLong(orderId), OrderStatus.REJECTED);
        Order order = orderService.getOrderById(Long.parseLong(orderId)).get();
        order.getItems().forEach(item -> {
            Long currentStock = phoneService.findStock(item.getPhone()).getStock();
            phoneService.updateStock(item.getPhone().getId(), item.getQuantity() + currentStock);
        });
        return ORDER_REJECTED;
    }
}

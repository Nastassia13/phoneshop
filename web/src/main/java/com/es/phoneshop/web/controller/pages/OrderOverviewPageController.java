package com.es.phoneshop.web.controller.pages;

import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview/{secureId}")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(@PathVariable String secureId, Model model) {
        model.addAttribute("order", orderService.getOrder(secureId));
        return "orderOverview";
    }
}

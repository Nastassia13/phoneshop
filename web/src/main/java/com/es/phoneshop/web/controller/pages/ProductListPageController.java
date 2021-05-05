package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.PhoneDao;
import com.es.core.model.cart.Cart;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;
import com.es.core.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;
    private static final int LIMIT = 20;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(@RequestParam(required = false) String query,
                                  @RequestParam(required = false) String sort,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false, defaultValue = "1") int page,
                                  Model model) {
        Cart cart = cartService.getCart();
        int offset = (page - 1) * LIMIT;
        List<Phone> phones = phoneDao.findAll(offset, LIMIT, query,
                SortField.getSortFieldByString(sort), SortOrder.getSortOrderByString(order));
        int countPages = phoneDao.countPages(LIMIT, query);
        model.addAttribute("phones", phones);
        model.addAttribute("pagesCount", countPages);
        model.addAttribute("cart", cart);
        return "productList";
    }
}

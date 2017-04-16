package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "cart")
public class CartController {

    @Autowired
    Cart cart;

    @RequestMapping(method = RequestMethod.GET)
    public String viewCart(Model model) {
        model.addAttribute("orderLines", cart.getOrderLines());
        return "cart/viewCart";
    }

}

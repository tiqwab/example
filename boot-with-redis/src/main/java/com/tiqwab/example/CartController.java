package com.tiqwab.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping(value = "cart")
public class CartController {

    @Autowired
    Cart cart;

    @RequestMapping(method = RequestMethod.GET)
    public String viewCart(Model model) {
        model.addAttribute(cart);
        log.info("Generate cart: {}", cart.toString());
        return "cart/viewCart";
    }

}

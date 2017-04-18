package com.tiqwab.example.app;

import com.tiqwab.example.DemoUserDetails;
import com.tiqwab.example.domain.model.Cart;
import com.tiqwab.example.domain.model.DemoOrder;
import com.tiqwab.example.domain.service.EmptyCartOrderException;
import com.tiqwab.example.domain.service.InvalidCartOrderException;
import com.tiqwab.example.domain.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    Cart cart;

    @RequestMapping(method = RequestMethod.GET, params = "confirm")
    public String confirm(@AuthenticationPrincipal DemoUserDetails user, Model model) {
        model.addAttribute("orderLines", cart.getOrderLines());
        if (cart.isEmpty()) {
            model.addAttribute("error", "買い物カゴが空です");
            return "cart/viewCart";
        }
        model.addAttribute("account", user.getAccount());
        model.addAttribute("signature", orderService.calcSignature(cart));
        return "order/confirm";
    }

    @RequestMapping(method=RequestMethod.POST, params = "signature")
    public String order(@AuthenticationPrincipal DemoUserDetails user,
                        @RequestParam String signature,
                        RedirectAttributes redirectAttributes) {
        DemoOrder order = orderService.purchase(user.getAccount(), cart, signature);
        redirectAttributes.addFlashAttribute("order", order);
        return "redirect:/order?finish";
    }

    @RequestMapping(method = RequestMethod.GET, params = "finish")
    public String finish() {
        return "order/finish";
    }

    @ExceptionHandler({EmptyCartOrderException.class, InvalidCartOrderException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleOrderException(RuntimeException e) {
        return new ModelAndView("order/error")
                .addObject("error", e.getMessage());
    }

}

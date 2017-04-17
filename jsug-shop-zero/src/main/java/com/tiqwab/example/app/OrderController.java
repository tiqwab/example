package com.tiqwab.example.app;

import com.tiqwab.example.DemoUserDetails;
import com.tiqwab.example.domain.model.Cart;
import com.tiqwab.example.domain.model.DemoOrder;
import com.tiqwab.example.domain.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        List<DemoOrder> orders = orderService.purchase(user.getAccount(), cart, signature);
        redirectAttributes.addFlashAttribute(orders);
        return "redirect:/order?finish";
    }

    @RequestMapping(method = RequestMethod.GET, params = "finish")
    public String finish() {
        return "order/finish";
    }

}

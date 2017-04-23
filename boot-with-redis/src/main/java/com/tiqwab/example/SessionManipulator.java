package com.tiqwab.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Slf4j
@Controller
@RequestMapping(value = "session")
public class SessionManipulator {

    @RequestMapping(method = RequestMethod.GET)
    public String checkCartInSession(HttpSession session, Model model) {
        Enumeration<String> attributes = session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            log.info(attributes.nextElement());
        }
        Cart cart = (Cart) session.getAttribute("scopedTarget.cart");
        log.info("Retrieved cart from session: {}", cart);
        if (cart != null) {
            model.addAttribute(cart);
        }
        return "session/viewSession";
    }

    @RequestMapping(value = "delete", method=RequestMethod.POST)
    public String invalidateSession(HttpSession session) {
        session.invalidate();
        return "redirect:/session";
    }

}

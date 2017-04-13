package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.Account;
import com.tiqwab.example.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("account")
public class AccountController {

    @Autowired
    AccountService accountService;

    // TODO: Check @ModelAttribute
    // When is a model created and deleted?
    @ModelAttribute
    AccountForm setupForm() {
        return new AccountForm();
    }

    @RequestMapping(value = "create", params = "form", method = RequestMethod.GET)
    String createForm() {
        return "account/createForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated AccountForm form, BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "account/createForm";
        }

        Account account = Account.builder()
                .name(form.getName())
                .password(Account.PASSWORD_ENCODER.encode(form.getPassword()))
                .email(form.getEmail())
                .birthDay(form.getBirthDay())
                .zip(form.getZip())
                .address(form.getAddress())
                .age(form.getAge())
                .build();
        accountService.register(account);

        // TODO: addFlashAttribute はリダイレクト先の controller メソッドや thymeleaf で使用したい場合に良さそう
        //       addAttribute はどうやら request parameter としてセットされる。なので リダイレクト先の controller メソッドで PathVariable や RequestParam として取得できる。
        attributes.addFlashAttribute(account);
        return "redirect:/account/create?finish"; // post-request-get (PRG) pattern
    }

    @RequestMapping(value = "create", params = "finish", method = RequestMethod.GET)
    String createFinish() {
        return "account/createFinish";
    }

}

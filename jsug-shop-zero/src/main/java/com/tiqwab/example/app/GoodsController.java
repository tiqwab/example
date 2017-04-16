package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.Cart;
import com.tiqwab.example.domain.model.Category;
import com.tiqwab.example.domain.model.Goods;
import com.tiqwab.example.domain.model.OrderLine;
import com.tiqwab.example.domain.service.CategoryService;
import com.tiqwab.example.domain.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    Cart cart;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return this.categoryService.findAll();
    }

    @ModelAttribute
    public AddToCartForm addToCartForm() {
        return new AddToCartForm();
    }

    @RequestMapping(value = "/")
    public String showGoods(@RequestParam(value = "categoryId", defaultValue = "1") Long categoryId,
                            @PageableDefault Pageable pageable, // TODO: What is @PageableDefault
                            Model model) {
        Page<Goods> page = goodsService.findByCategoryId(categoryId, pageable);
        model.addAttribute("page", page);
        model.addAttribute("categoryId", categoryId);
        return "goods/showGoods";
    }

    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    public String addToCart(@Validated AddToCartForm addToCartForm,
                            BindingResult bindingResult,
                            @PageableDefault Pageable pageable,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return showGoods(addToCartForm.getCategoryId(), pageable, model);
        }
        Goods goods = goodsService.findOne(addToCartForm.getGoodsId());
        cart.add(OrderLine.builder()
                          .goods(goods)
                          .quantity(addToCartForm.getQuantity())
                          .build());
        return "redirect:/cart";
    }

}

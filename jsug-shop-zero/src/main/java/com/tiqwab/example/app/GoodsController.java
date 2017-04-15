package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.Category;
import com.tiqwab.example.domain.model.Goods;
import com.tiqwab.example.domain.service.CategoryService;
import com.tiqwab.example.domain.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    GoodsService goodsService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return this.categoryService.findAll();
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

}

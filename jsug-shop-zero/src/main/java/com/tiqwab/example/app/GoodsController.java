package com.tiqwab.example.app;

import com.tiqwab.example.domain.model.Category;
import com.tiqwab.example.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return this.categoryService.findAll();
    }

    @RequestMapping(value = "/")
    public String showGoods(@RequestParam(defaultValue = "1") Integer categoryId,
                            Model model) {
        model.addAttribute("categoryId", categoryId);
        return "goods/showGoods";
    }

}

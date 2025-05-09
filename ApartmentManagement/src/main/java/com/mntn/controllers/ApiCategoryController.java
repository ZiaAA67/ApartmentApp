package com.mntn.controllers;

import com.mntn.pojo.Category;
import com.mntn.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getCategories(@RequestParam(name = "isActive", required = false) Boolean isActive) {
        if (isActive == null) {
            isActive = true;
        }
        return categoryService.getCategories(isActive);
    }

}

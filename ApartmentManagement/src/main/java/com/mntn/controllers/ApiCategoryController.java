package com.mntn.controllers;

import com.mntn.pojo.Category;
import com.mntn.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(@RequestParam(name = "isActive", required = false) Boolean isActive) {
        return new ResponseEntity<>(this.categoryService.getCategories(isActive), HttpStatus.OK);
    }
}

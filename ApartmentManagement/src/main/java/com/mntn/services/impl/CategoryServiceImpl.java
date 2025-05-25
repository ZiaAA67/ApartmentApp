package com.mntn.services.impl;

import com.mntn.pojo.Category;
import com.mntn.repositories.CategoryRepository;
import com.mntn.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(Boolean isActive) {
        if (isActive == null) {
            isActive = true;
        }
        return categoryRepository.getCategories(isActive);
    }

    @Override
    public Category getCateById(String id) {
        return categoryRepository.findById(id);
    }
}

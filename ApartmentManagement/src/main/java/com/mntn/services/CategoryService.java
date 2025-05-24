package com.mntn.services;

import com.mntn.pojo.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getCategories(Boolean isActive);

    Category getCateById(String id);
}

package com.mntn.repositories;

import com.mntn.pojo.Category;
import java.util.List;

public interface CategoryRepository {

    List<Category> getCategories(Boolean isActive);
    
    Category findById(String id);
}

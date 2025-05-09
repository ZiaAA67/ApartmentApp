package com.mntn.repositories.impl;

import com.mntn.pojo.Category;
import com.mntn.repositories.CategoryRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Category> getCategories(Boolean isActive) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Category.findByIsActive", Category.class);
        q.setParameter("isActive", isActive);

        return q.getResultList();
    }
}

package com.mntn.repositories.impl;

import com.mntn.pojo.Method;
import com.mntn.repositories.MethodRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MethodRepositoryImpl implements MethodRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Method> getMethod(Boolean isActive) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Method.findByIsActive", Method.class);
        q.setParameter("isActive", isActive);

        return q.getResultList();
    }

}

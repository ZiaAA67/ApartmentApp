package com.mntn.repositories.impl;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.repositories.TransactionRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Transaction> getTransactionsByUserIdStatusAndCategory(String userId, String status, String categoryId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Transaction.findByUserIdStatusAndCategory", Transaction.class);
        q.setParameter("userId", userId);
        q.setParameter("status", status);
        q.setParameter("categoryId", categoryId);

        return q.getResultList();
    }

    @Override
    public List<Transaction> getTransactionsByUserId(String userId) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Transaction.findByUserId", Transaction.class);
        q.setParameter("userId", userId);

        return q.getResultList();
    }

}

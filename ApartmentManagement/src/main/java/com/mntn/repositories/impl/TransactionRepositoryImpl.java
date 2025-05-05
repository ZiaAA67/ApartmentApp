/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author nghia
 */
@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Transaction> getTransactionsByUserAndStatus(User user, String status) {
        Session s = factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Transaction.findByUserIdAndStatus", Transaction.class);
        q.setParameter("userId", user);
        q.setParameter("status", status);

        return q.getResultList();
    }
}

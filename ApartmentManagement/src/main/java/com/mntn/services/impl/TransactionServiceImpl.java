/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.services.impl;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.repositories.TransactionRepository;
import com.mntn.services.TransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author nghia
 */
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactionsByUserAndStatus(User user, String status) {
        return transactionRepository.getTransactionsByUserAndStatus(user, status);
    }

}

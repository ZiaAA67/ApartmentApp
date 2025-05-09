/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.services;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.repositories.TransactionRepository;
import java.util.List;

/**
 *
 * @author nghia
 */
public interface TransactionService{

    List<Transaction> getTransactionsByUserIdStatusAndCategory(String userId, String status, String categoryId);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.repositories;

import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import java.util.List;

/**
 *
 * @author nghia
 */
public interface TransactionRepository {
    List<Transaction> getTransactionsByUserAndStatus(User user, String status);
}

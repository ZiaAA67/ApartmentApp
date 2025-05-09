/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.repositories;

import com.mntn.pojo.Transaction;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> getTransactionsByUserIdStatusAndCategory(String userId, String status, String categoryId);
}

package com.mntn.services.impl;

import com.mntn.pojo.Transaction;
import com.mntn.repositories.TransactionRepository;
import com.mntn.services.TransactionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions(Map<String, String> params) {

        String userId = params.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu userId!");
        }

        return transactionRepository.getTransactions(params);
    }
}

package com.mntn.services;

import com.mntn.dto.TransactionDTO;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.momo.PaymentResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface TransactionService {

    List<Transaction> getTransactions(Map<String, String> params);

    List<Transaction> getTransactionsByAdmin(Map<String, String> params);

    List<Transaction> createTransactions(List<TransactionDTO> dataList);

    Transaction updateTransactionImage(String transactionId, Map<String, String> updates, MultipartFile momoImage);

    Transaction updateTransactionStatus(String transactionId);

    Transaction processMomoIPN(Map<String, String> payload);

    PaymentResponse payTransaction(String transactionId) throws RuntimeException;
}

package com.mntn.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mntn.configs.momo.Environment;
import com.mntn.dto.TransactionDTO;
import com.mntn.enums.RequestType;
import com.mntn.pojo.Apartment;
import com.mntn.pojo.Category;
import com.mntn.pojo.Transaction;
import com.mntn.pojo.User;
import com.mntn.pojo.momo.PaymentResponse;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.repositories.CategoryRepository;
import com.mntn.repositories.TransactionRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.TransactionService;
import com.mntn.utils.momo.LogUtils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.multipart.MultipartFile;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreateOrderMomo createOrderMomo;

    @Autowired
    private Environment environment;

    // Xử lí thanh toán momo
    @Override
    public PaymentResponse payTransaction(String transactionId) throws RuntimeException {

        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("transactionId không hợp lệ!");
        }

        Transaction transaction = transactionRepository.getTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Không tìm thấy giao dịch!");
        }

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = transactionId + "_" + System.currentTimeMillis();
        BigDecimal amount = transaction.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền giao dịch không hợp lệ!");
        }

        String returnURL = "http://localhost:3000/pay-transactions";
        String notifyURL = "http://localhost:8080/ApartmentManagement/api/momo/callback";
        String orderInfo = "Giao dịch " + transaction.getCategoryId().getName();

        long amountLong = amount.setScale(0, BigDecimal.ROUND_DOWN).longValue();
        try {
            PaymentResponse paymentResponse = createOrderMomo.process(
                    environment, orderId, requestId, String.valueOf(amountLong),
                    orderInfo, returnURL, notifyURL,
                    "", RequestType.PAY_WITH_ATM, Boolean.TRUE
            );
            return paymentResponse;
        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("Lõi phần TransSerImpl!!" + ex.getMessage(), ex);
        }
    }

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime due = now.plusMonths(1);

    // Tạo các transactions
    @Override
    public List<Transaction> createTransactions(List<TransactionDTO> dataList) {
        List<Transaction> result = new ArrayList<>();

        for (TransactionDTO dto : dataList) {
            BigDecimal amount = dto.getAmount();
            String apartmentId = dto.getApartmentId();
            String categoryId = dto.getCategoryId();

            Apartment a = apartmentRepo.getApartmentById(apartmentId);
            Category c = categoryRepo.findById(categoryId);

            if (a != null && c != null) {
                Transaction t = new Transaction();
                t.setId(UUID.randomUUID().toString());
                t.setAmount(amount);
                t.setCreatedDate(new Date());
                t.setDueDate(Timestamp.valueOf(due));
                t.setStatus("unpaid");
                t.setApartmentId(a);
                t.setCategoryId(c);
                t.setUserId(null);
                t.setMethodId(null);
                t.setImage(null);

                result.add(transactionRepository.addTransaction(t));
            }
        }
        return result;
    }

    @Autowired
    private Cloudinary cloudinary;

    // Cập nhập transaction khi nạp momo img
    @Override
    public Transaction updateTransactionImage(String transactionId, Map<String, String> updates, MultipartFile momoImage) {
        if (transactionId == null) {
            throw new IllegalArgumentException("Trans Id null!");
        }

        Transaction t = transactionRepository.getTransactionById(transactionId);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy transaction với ID: " + transactionId);
        }

        if (momoImage != null && !momoImage.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(momoImage.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                t.setImage(res.get("secure_url").toString());
                t.setUpdatedDate(new Date());
                t.setStatus("pending");

                return transactionRepository.updateTransaction(t);
            } catch (IOException ex) {
                Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Upload ảnh thất bại: " + ex.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Ảnh momoImage bị thiếu!");
        }
    }

    @Override
    public Transaction processMomoIPN(Map<String, String> payload
    ) {
        String orderId = payload.get("orderId");
        String resultCode = payload.get("resultCode");
        String transId = orderId.split("_")[0];

        if (orderId == null || resultCode == null) {
            throw new IllegalArgumentException("IPN không hợp lệ");
        }

        if ("0".equals(resultCode)) {
            Transaction t = transactionRepository.getTransactionById(transId);
            if (t == null) {
                throw new IllegalArgumentException("Không tìm thấy transaction");
            }
            t.setStatus("completed");
            t.setUpdatedDate(new Date());

            return transactionRepository.updateTransaction(t);
        }

        return null;
    }

    @Override
    public Transaction updateTransactionStatus(String transactionId, String status) {
        if (transactionId == null || status == null) {
            throw new IllegalArgumentException("Transaction ID hoặc Status không được null!");
        }

        Transaction t = transactionRepository.getTransactionById(transactionId);
        if (t == null) {
            throw new IllegalArgumentException("Không tìm thấy transaction với ID: " + transactionId);
        }

        t.setStatus(status);
        t.setUpdatedDate(new Date());
        return transactionRepository.updateTransaction(t);
    }

    @Override
    public List<Transaction> getTransactionsByApartment(String apartmentId, Map<String, String> params) {
        String userId = params.get("userId");

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu userId!");
        }

        if (apartmentId == null || apartmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu apartmentId!");
        }

        Apartment apartment = apartmentRepo.getApartmentById(apartmentId);
        if (apartment == null) {
            throw new IllegalArgumentException("Căn hộ không tồn tại!");
        }
        User owner = apartment.getCurrentOwnerId();
        String ownerId = owner.getId();
        if ((userId.equals(ownerId))) {
            Map<String, String> newParams = new HashMap<>();
            newParams.put("apartmentId", apartmentId);

            return transactionRepository.getTransactions(newParams);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Transaction> getTransactions(Map<String, String> params) {
        String userId = params.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Thiếu userId!");
        }
        return transactionRepository.getTransactions(params);
    }

    // Lấy các transaction
    @Override
    public List<Transaction> getTransactionsByAdmin(Map<String, String> params) {
        return transactionRepository.getTransactions(params);
    }
}

package com.mntn.dto;

import java.math.BigDecimal;

public class TransactionDTO {

    private BigDecimal amount;
    private String categoryId;
    private String apartmentId;

    public TransactionDTO() {
    }

    public TransactionDTO(BigDecimal amount, String categoryId, String apartmentId) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.apartmentId = apartmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }
}

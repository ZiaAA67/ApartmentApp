/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author nghia
 */
@Entity
@Table(name = "transaction")
@NamedQueries({
    @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
    @NamedQuery(name = "Transaction.findById", query = "SELECT t FROM Transaction t WHERE t.id = :id"),
    @NamedQuery(name = "Transaction.findByAmount", query = "SELECT t FROM Transaction t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transaction.findByCreatedDate", query = "SELECT t FROM Transaction t WHERE t.createdDate = :createdDate"),
    @NamedQuery(name = "Transaction.findByDueDate", query = "SELECT t FROM Transaction t WHERE t.dueDate = :dueDate"),
    @NamedQuery(name = "Transaction.findByUpdatedDate", query = "SELECT t FROM Transaction t WHERE t.updatedDate = :updatedDate"),
    @NamedQuery(name = "Transaction.findByStatus", query = "SELECT t FROM Transaction t WHERE t.status = :status"),
    @NamedQuery(name = "Transaction.findByImage", query = "SELECT t FROM Transaction t WHERE t.image = :image")})
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "id")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Size(max = 9)
    @Column(name = "status")
    private String status;
    @Size(max = 333)
    @Column(name = "image")
    private String image;
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Apartment apartmentId;
    @JsonIgnoreProperties({"transactions"})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private Category categoryId;
    @JsonIgnoreProperties({"transactions"})
    @JoinColumn(name = "method_id", referencedColumnName = "id")
    @ManyToOne
    private Method methodId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public Transaction() {
    }

    public Transaction(String id) {
        this.id = id;
    }

    public Transaction(String id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Apartment getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Apartment apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public Method getMethodId() {
        return methodId;
    }

    public void setMethodId(Method methodId) {
        this.methodId = methodId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.Transaction[ id=" + id + " ]";
    }

}

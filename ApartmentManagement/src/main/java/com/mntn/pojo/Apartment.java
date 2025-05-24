/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author macbook
 */
@Entity
@Table(name = "apartment")
@NamedQueries({
    @NamedQuery(name = "Apartment.findAll", query = "SELECT a FROM Apartment a"),
    @NamedQuery(name = "Apartment.findById", query = "SELECT a FROM Apartment a WHERE a.id = :id"),
    @NamedQuery(name = "Apartment.findByNumber", query = "SELECT a FROM Apartment a WHERE a.number = :number"),
    @NamedQuery(name = "Apartment.findByBlock", query = "SELECT a FROM Apartment a WHERE a.block = :block"),
    @NamedQuery(name = "Apartment.findByFloor", query = "SELECT a FROM Apartment a WHERE a.floor = :floor"),
    @NamedQuery(name = "Apartment.findByArea", query = "SELECT a FROM Apartment a WHERE a.area = :area"),
    @NamedQuery(name = "Apartment.findByBathroom", query = "SELECT a FROM Apartment a WHERE a.bathroom = :bathroom"),
    @NamedQuery(name = "Apartment.findByBedroom", query = "SELECT a FROM Apartment a WHERE a.bedroom = :bedroom"),
    @NamedQuery(name = "Apartment.findByStatus", query = "SELECT a FROM Apartment a WHERE a.status = :status"),
    @NamedQuery(name = "Apartment.findByIsActive", query = "SELECT a FROM Apartment a WHERE a.isActive = :isActive")})
public class Apartment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "number")
    private String number;
    @Basic(optional = false)
    @Column(name = "block")
    private String block;
    @Basic(optional = false)
    @Column(name = "floor")
    private String floor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "area")
    private BigDecimal area;
    @Column(name = "bathroom")
    private Integer bathroom;
    @Column(name = "bedroom")
    private Integer bedroom;
    @Column(name = "status")
    private String status;
    @Column(name = "is_active")
    private Boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apartmentId")
    @JsonIgnore
    private Set<Delivery> deliverySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apartmentId")
    @JsonIgnore
    private Set<ApartmentHistory> apartmentHistorySet;
    @JoinColumn(name = "current_owner_id", referencedColumnName = "id")
    @ManyToOne
    private User currentOwnerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apartmentId")
    @JsonIgnore
    private Set<Transaction> transactionSet;

    public Apartment() {
    }

    public Apartment(String id) {
        this.id = id;
    }

    public Apartment(String id, String number, String block, String floor, BigDecimal area) {
        this.id = id;
        this.number = number;
        this.block = block;
        this.floor = floor;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getBathroom() {
        return bathroom;
    }

    public void setBathroom(Integer bathroom) {
        this.bathroom = bathroom;
    }

    public Integer getBedroom() {
        return bedroom;
    }

    public void setBedroom(Integer bedroom) {
        this.bedroom = bedroom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Delivery> getDeliverySet() {
        return deliverySet;
    }

    public void setDeliverySet(Set<Delivery> deliverySet) {
        this.deliverySet = deliverySet;
    }

    public Set<ApartmentHistory> getApartmentHistorySet() {
        return apartmentHistorySet;
    }

    public void setApartmentHistorySet(Set<ApartmentHistory> apartmentHistorySet) {
        this.apartmentHistorySet = apartmentHistorySet;
    }

    public User getCurrentOwnerId() {
        return currentOwnerId;
    }

    public void setCurrentOwnerId(User currentOwnerId) {
        this.currentOwnerId = currentOwnerId;
    }

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
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
        if (!(object instanceof Apartment)) {
            return false;
        }
        Apartment other = (Apartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.Apartment[ id=" + id + " ]";
    }
    
}

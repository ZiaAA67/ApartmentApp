/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author macbook
 */
@Entity
@Table(name = "delivery")
@NamedQueries({
    @NamedQuery(name = "Delivery.findAll", query = "SELECT d FROM Delivery d"),
    @NamedQuery(name = "Delivery.findById", query = "SELECT d FROM Delivery d WHERE d.id = :id"),
    @NamedQuery(name = "Delivery.findByRecipientName", query = "SELECT d FROM Delivery d WHERE d.recipientName = :recipientName"),
    @NamedQuery(name = "Delivery.findByArrivedAt", query = "SELECT d FROM Delivery d WHERE d.arrivedAt = :arrivedAt"),
    @NamedQuery(name = "Delivery.findByDeliveredAt", query = "SELECT d FROM Delivery d WHERE d.deliveredAt = :deliveredAt"),
    @NamedQuery(name = "Delivery.findByStatus", query = "SELECT d FROM Delivery d WHERE d.status = :status")})
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "recipient_name")
    private String recipientName;
    @Lob
    @Column(name = "package_description")
    private String packageDescription;
    @Column(name = "arrived_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivedAt;
    @Column(name = "delivered_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredAt;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Lob
    @Column(name = "notes")
    private String notes;
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Apartment apartmentId;

    public Delivery() {
    }

    public Delivery(String id) {
        this.id = id;
    }

    public Delivery(String id, String recipientName, String status) {
        this.id = id;
        this.recipientName = recipientName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public Date getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(Date arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Apartment getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Apartment apartmentId) {
        this.apartmentId = apartmentId;
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
        if (!(object instanceof Delivery)) {
            return false;
        }
        Delivery other = (Delivery) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.Delivery[ id=" + id + " ]";
    }
    
}

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
@Table(name = "apartment_history")
@NamedQueries({
    @NamedQuery(name = "ApartmentHistory.findAll", query = "SELECT a FROM ApartmentHistory a"),
    @NamedQuery(name = "ApartmentHistory.findById", query = "SELECT a FROM ApartmentHistory a WHERE a.id = :id"),
    @NamedQuery(name = "ApartmentHistory.findByAction", query = "SELECT a FROM ApartmentHistory a WHERE a.action = :action"),
    @NamedQuery(name = "ApartmentHistory.findByActionDate", query = "SELECT a FROM ApartmentHistory a WHERE a.actionDate = :actionDate")})
public class ApartmentHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "action")
    private String action;
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Lob
    @Column(name = "notes")
    private String notes;
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Apartment apartmentId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public ApartmentHistory() {
    }

    public ApartmentHistory(String id) {
        this.id = id;
    }

    public ApartmentHistory(String id, String action) {
        this.id = id;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
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
        if (!(object instanceof ApartmentHistory)) {
            return false;
        }
        ApartmentHistory other = (ApartmentHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.ApartmentHistory[ id=" + id + " ]";
    }
    
}

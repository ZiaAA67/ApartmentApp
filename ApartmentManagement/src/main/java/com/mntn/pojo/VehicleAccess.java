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
@Table(name = "vehicle_access")
@NamedQueries({
    @NamedQuery(name = "VehicleAccess.findAll", query = "SELECT v FROM VehicleAccess v"),
    @NamedQuery(name = "VehicleAccess.findById", query = "SELECT v FROM VehicleAccess v WHERE v.id = :id"),
    @NamedQuery(name = "VehicleAccess.findByType", query = "SELECT v FROM VehicleAccess v WHERE v.type = :type"),
    @NamedQuery(name = "VehicleAccess.findByNumber", query = "SELECT v FROM VehicleAccess v WHERE v.number = :number"),
    @NamedQuery(name = "VehicleAccess.findByBrand", query = "SELECT v FROM VehicleAccess v WHERE v.brand = :brand"),
    @NamedQuery(name = "VehicleAccess.findByModel", query = "SELECT v FROM VehicleAccess v WHERE v.model = :model"),
    @NamedQuery(name = "VehicleAccess.findByColor", query = "SELECT v FROM VehicleAccess v WHERE v.color = :color"),
    @NamedQuery(name = "VehicleAccess.findByIsActive", query = "SELECT v FROM VehicleAccess v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VehicleAccess.findByIsPermanent", query = "SELECT v FROM VehicleAccess v WHERE v.isPermanent = :isPermanent"),
    @NamedQuery(name = "VehicleAccess.findByCreatedDate", query = "SELECT v FROM VehicleAccess v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VehicleAccess.findByAccessTime", query = "SELECT v FROM VehicleAccess v WHERE v.accessTime = :accessTime")})
public class VehicleAccess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "number")
    private String number;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "color")
    private String color;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_permanent")
    private Boolean isPermanent;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "access_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessTime;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public VehicleAccess() {
    }

    public VehicleAccess(String id) {
        this.id = id;
    }

    public VehicleAccess(String id, String type, String number) {
        this.id = id;
        this.type = type;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(Boolean isPermanent) {
        this.isPermanent = isPermanent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
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
        if (!(object instanceof VehicleAccess)) {
            return false;
        }
        VehicleAccess other = (VehicleAccess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.VehicleAccess[ id=" + id + " ]";
    }
    
}

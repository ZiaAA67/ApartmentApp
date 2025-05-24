/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;

/**
 *
 * @author macbook
 */
@Entity
@Table(name = "apartment_image")
@NamedQueries({
    @NamedQuery(name = "ApartmentImage.findAll", query = "SELECT a FROM ApartmentImage a"),
    @NamedQuery(name = "ApartmentImage.findById", query = "SELECT a FROM ApartmentImage a WHERE a.id = :id"),
    @NamedQuery(name = "ApartmentImage.findByImageUrl", query = "SELECT a FROM ApartmentImage a WHERE a.imageUrl = :imageUrl"),
    @NamedQuery(name = "ApartmentImage.findByCreatedDate", query = "SELECT a FROM ApartmentImage a WHERE a.createdDate = :createdDate")})
public class ApartmentImage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "apartment_id")
    private String apartmentId;

    public ApartmentImage() {
    }

    public ApartmentImage(String id) {
        this.id = id;
    }

    public ApartmentImage(String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
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
        if (!(object instanceof ApartmentImage)) {
            return false;
        }
        ApartmentImage other = (ApartmentImage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.ApartmentImage[ id=" + id + " ]";
    }
    
}

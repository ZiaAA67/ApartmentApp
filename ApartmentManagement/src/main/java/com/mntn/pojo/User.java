/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author macbook
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByGender", query = "SELECT u FROM User u WHERE u.gender = :gender"),
    @NamedQuery(name = "User.findByBirth", query = "SELECT u FROM User u WHERE u.birth = :birth"),
    @NamedQuery(name = "User.findByIdentityNumber", query = "SELECT u FROM User u WHERE u.identityNumber = :identityNumber"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
    @NamedQuery(name = "User.findByAddress", query = "SELECT u FROM User u WHERE u.address = :address"),
    @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
    @NamedQuery(name = "User.findByIsFirstLogin", query = "SELECT u FROM User u WHERE u.isFirstLogin = :isFirstLogin"),
    @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive"),
    @NamedQuery(name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "User.findByCreatedDate", query = "SELECT u FROM User u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "User.findByUpdatedDate", query = "SELECT u FROM User u WHERE u.updatedDate = :updatedDate")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // ẩn khi deserialization, không hiển thị pass khi get api
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private Short gender;
    @Column(name = "birth")
    @Temporal(TemporalType.DATE)
    private Date birth;
    @Basic(optional = false)
    @Column(name = "identity_number")
    private String identityNumber;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "role")
    private String role;
    @Column(name = "is_first_login")
    private Boolean isFirstLogin;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "avatar")
    private String avatar;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<SurveyResponse> surveyResponseSet;
    @OneToMany(mappedBy = "userId")
    @JsonIgnore
    private Set<AccessCard> accessCardSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<ApartmentHistory> apartmentHistorySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Complaint> complaintSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Survey> surveySet;
    @OneToMany(mappedBy = "currentOwnerId")
    @JsonIgnore
    private Set<Apartment> apartmentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    @JsonIgnore
    private Set<Transaction> transactionSet;
    @OneToMany(mappedBy = "userId")
    @JsonIgnore
    private Set<VehicleAccess> vehicleAccessSet;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String username, String password, String firstName, String lastName, String identityNumber, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityNumber = identityNumber;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<SurveyResponse> getSurveyResponseSet() {
        return surveyResponseSet;
    }

    public void setSurveyResponseSet(Set<SurveyResponse> surveyResponseSet) {
        this.surveyResponseSet = surveyResponseSet;
    }

    public Set<AccessCard> getAccessCardSet() {
        return accessCardSet;
    }

    public void setAccessCardSet(Set<AccessCard> accessCardSet) {
        this.accessCardSet = accessCardSet;
    }

    public Set<ApartmentHistory> getApartmentHistorySet() {
        return apartmentHistorySet;
    }

    public void setApartmentHistorySet(Set<ApartmentHistory> apartmentHistorySet) {
        this.apartmentHistorySet = apartmentHistorySet;
    }

    public Set<Complaint> getComplaintSet() {
        return complaintSet;
    }

    public void setComplaintSet(Set<Complaint> complaintSet) {
        this.complaintSet = complaintSet;
    }

    public Set<Survey> getSurveySet() {
        return surveySet;
    }

    public void setSurveySet(Set<Survey> surveySet) {
        this.surveySet = surveySet;
    }

    public Set<Apartment> getApartmentSet() {
        return apartmentSet;
    }

    public void setApartmentSet(Set<Apartment> apartmentSet) {
        this.apartmentSet = apartmentSet;
    }

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    public Set<VehicleAccess> getVehicleAccessSet() {
        return vehicleAccessSet;
    }

    public void setVehicleAccessSet(Set<VehicleAccess> vehicleAccessSet) {
        this.vehicleAccessSet = vehicleAccessSet;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.User[ id=" + id + " ]";
    }
    
}

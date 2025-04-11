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
@Table(name = "survey_response")
@NamedQueries({
    @NamedQuery(name = "SurveyResponse.findAll", query = "SELECT s FROM SurveyResponse s"),
    @NamedQuery(name = "SurveyResponse.findById", query = "SELECT s FROM SurveyResponse s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyResponse.findBySubmittedDate", query = "SELECT s FROM SurveyResponse s WHERE s.submittedDate = :submittedDate")})
public class SurveyResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Lob
    @Column(name = "response")
    private String response;
    @Column(name = "submitted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedDate;
    @JoinColumn(name = "survey_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Survey surveyId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public SurveyResponse() {
    }

    public SurveyResponse(String id) {
        this.id = id;
    }

    public SurveyResponse(String id, String response) {
        this.id = id;
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Survey getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Survey surveyId) {
        this.surveyId = surveyId;
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
        if (!(object instanceof SurveyResponse)) {
            return false;
        }
        SurveyResponse other = (SurveyResponse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.SurveyResponse[ id=" + id + " ]";
    }
    
}

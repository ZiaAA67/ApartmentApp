/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mntn.pojo;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 *
 * @author macbook
 */
@Entity
@Table(name = "survey_option")
@NamedQueries({
    @NamedQuery(name = "SurveyOption.findAll", query = "SELECT s FROM SurveyOption s"),
    @NamedQuery(name = "SurveyOption.findById", query = "SELECT s FROM SurveyOption s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyOption.findByContent", query = "SELECT s FROM SurveyOption s WHERE s.content = :content")})
public class SurveyOption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "content")
    private String content;
    @Column(name = "survey_id")
    private String surveyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "optionId")
    @JsonIgnore
    private Set<SurveyResponse> surveyResponseSet;
    @Transient
    private int votes;

    public SurveyOption() {
    }

    public SurveyOption(String id) {
        this.id = id;
    }

    public SurveyOption(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<SurveyResponse> getSurveyResponseSet() {
        return surveyResponseSet;
    }

    public void setSurveyResponseSet(Set<SurveyResponse> surveyResponseSet) {
        this.surveyResponseSet = surveyResponseSet;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
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
        if (!(object instanceof SurveyOption)) {
            return false;
        }
        SurveyOption other = (SurveyOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mntn.pojo.SurveyOption[ id=" + id + " ]";
    }
    
}

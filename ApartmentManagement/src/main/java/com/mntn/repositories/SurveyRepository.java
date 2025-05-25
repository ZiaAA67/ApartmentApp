package com.mntn.repositories;

import com.mntn.pojo.Survey;

import java.util.List;

public interface SurveyRepository {
    Survey createSurvey(Survey sv);
    Survey getSurveyById(String id);
    List<Survey> getListSurvey();
}

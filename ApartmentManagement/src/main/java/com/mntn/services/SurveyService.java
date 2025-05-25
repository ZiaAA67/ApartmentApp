package com.mntn.services;

import com.mntn.pojo.Survey;

import java.util.List;
import java.util.Map;

public interface SurveyService {
    Survey createSurvey(Map<String, String> params);
    Survey getSurveyById(String id);
    List<Survey> getListSurvey();
}

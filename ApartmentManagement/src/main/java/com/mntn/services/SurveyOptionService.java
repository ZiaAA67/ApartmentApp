package com.mntn.services;

import com.mntn.pojo.SurveyOption;

import java.util.List;
import java.util.Map;

public interface SurveyOptionService {
    SurveyOption createOption(String surveyId, Map<String, String> params);
    SurveyOption getOptionById(String id);
    List<SurveyOption> getListOption(String surveyId);
}

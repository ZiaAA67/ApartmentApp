package com.mntn.services;

import com.mntn.pojo.SurveyResponse;

import java.util.List;
import java.util.Map;

public interface SurveyResponseService {
    SurveyResponse createResponse(String surveyId, String optionId, Map<String, String> params);
    List<SurveyResponse> getListResponse(String surveyId, String optionId);
    List<SurveyResponse> getUserResponse(String userId);
}

package com.mntn.repositories;

import com.mntn.pojo.SurveyResponse;

import java.util.List;

public interface SurveyResponseRepository {
    SurveyResponse createResponse(SurveyResponse res);
    List<SurveyResponse> getListResponse(String surveyId, String optionId);
    List<SurveyResponse> getUserResponse(String userId);
}

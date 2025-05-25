package com.mntn.controllers;

import com.mntn.pojo.SurveyOption;
import com.mntn.pojo.SurveyResponse;
import com.mntn.services.SurveyResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiSurveyResponseController {
    @Autowired
    private SurveyResponseService resService;

    @GetMapping("/secure/survey/{surveyId}/option/{optionId}/response")
    public ResponseEntity<List<SurveyResponse>> getListSurveyOption(@PathVariable(value = "surveyId") String surveyId,
                                                                    @PathVariable(value = "optionId") String optionId) {
        return new ResponseEntity<>(this.resService.getListResponse(surveyId, optionId), HttpStatus.OK);
    }

    @PostMapping("/secure/survey/{surveyId}/option/{optionId}/response")
    public ResponseEntity<Object> createSurveyResponse(@PathVariable(value = "surveyId") String surveyId,
                                                       @PathVariable(value = "optionId") String optionId,
                                                     @RequestBody Map<String, String> params) {
        try {
            return new ResponseEntity<>(this.resService.createResponse(surveyId, optionId, params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

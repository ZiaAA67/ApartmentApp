package com.mntn.controllers;

import com.mntn.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiStatsController {
    @Autowired
    private StatsService statsService;

    @GetMapping("vehicle/status")
    public ResponseEntity<List<Map<String, Object>>> getVehicleStatusStats() {
        return new ResponseEntity<>(this.statsService.statsVehicleByStatus(), HttpStatus.OK);
    }

    @GetMapping("user/status")
    public ResponseEntity<List<Map<String, Object>>> getUserStats() {
        return new ResponseEntity<>(this.statsService.statsActiveUsers(), HttpStatus.OK);
    }
}

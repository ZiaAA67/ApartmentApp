package com.mntn.services;

import java.util.List;
import java.util.Map;

public interface StatsService {
    List<Map<String, Object>> statsVehicleByStatus();
    List<Map<String, Object>> statsActiveUsers();
}

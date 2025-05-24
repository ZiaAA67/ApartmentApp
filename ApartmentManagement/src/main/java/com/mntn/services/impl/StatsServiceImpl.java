package com.mntn.services.impl;

import com.mntn.repositories.StatsRepository;
import com.mntn.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private StatsRepository statsRepo;

    @Override
    public List<Map<String, Object>> statsVehicleByStatus() {
        List<Object[]> rawStats = statsRepo.statsVehicleByStatus();

        List<Map<String, Object>> stats = new ArrayList<>();
        for (Object[] row : rawStats) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", row[0]);
            map.put("count", row[1]);
            stats.add(map);
        }
        return stats;
    }

    @Override
    public List<Map<String, Object>> statsActiveUsers() {
        List<Object[]> rawStats = statsRepo.statsActiveUsers();

        List<Map<String, Object>> stats = new ArrayList<>();
        for (Object[] row : rawStats) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", row[0]);
            map.put("count", row[1]);
            stats.add(map);
        }
        return stats;
    }
}

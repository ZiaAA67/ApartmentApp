package com.mntn.repositories;

import java.util.List;
import java.util.Map;

public interface StatsRepository {
    List<Object[]> statsVehicleByStatus();
    List<Object[]> statsActiveUsers();
}

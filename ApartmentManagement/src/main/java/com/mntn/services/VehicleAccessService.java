package com.mntn.services;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.VehicleAccess;

import java.util.List;
import java.util.Map;

public interface VehicleAccessService {
    PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params);
    List<VehicleAccess> getListVehicleAccessByUserId(String userId);
    VehicleAccess addVehicleAccess(Map<String, String> params);
    VehicleAccess updateVehicleAccess(String id, Map<String, String> updates);
}

package com.mntn.repositories;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.VehicleAccess;

import java.util.List;
import java.util.Map;

public interface VehicleAccessRepository {
    PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params);
    List<VehicleAccess> getListVehicleAccessByUserId(String userId);
    VehicleAccess addVehicleAccess(VehicleAccess v);
    VehicleAccess updateVehicleAccess(VehicleAccess v);
    VehicleAccess getVehicleAccessById(String id);
}

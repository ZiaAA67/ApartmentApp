package com.mntn.services.impl;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.repositories.UserRepository;
import com.mntn.repositories.VehicleAccessRepository;
import com.mntn.services.VehicleAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class VehicleAccessServiceIpml implements VehicleAccessService {
    @Autowired
    private VehicleAccessRepository vehicleAccessRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public PaginatedResponse<VehicleAccess> getListVehicleAccess(Map<String, String> params) {
        return vehicleAccessRepo.getListVehicleAccess(params);
    }

    @Override
    public List<VehicleAccess> getListVehicleAccessByUserId(String userId) {
        return vehicleAccessRepo.getListVehicleAccessByUserId(userId);
    }

    @Override
    public VehicleAccess addVehicleAccess(Map<String, String> params) {
        VehicleAccess v = new VehicleAccess();

        v.setId(UUID.randomUUID().toString());

        String type = params.get("type");
        if (type == null)
            throw new IllegalArgumentException("Loại xe chưa được cung cấp!");
        v.setType(type);

        // xe đạp không cần biển số
        String number = params.get("number");
        if (!"bicycle".equals(type)) {
            if (number == null || number.isEmpty())
                throw new IllegalArgumentException("Biển số xe chưa được cung cấp!");
            v.setNumber(number);
        } else {
            v.setNumber("");
        }

        v.setBrand(params.getOrDefault("brand", null));
        v.setModel(params.getOrDefault("model", null));
        v.setColor(params.getOrDefault("color", null));

        boolean isPermanent = Boolean.parseBoolean(params.getOrDefault("isPermanent", "false"));
        v.setIsPermanent(isPermanent);
        if(isPermanent) {
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime time = now.plusMonths(6);
            v.setAccessTime(Date.from(time.toInstant()));
        } else {
            Instant now = Instant.now();
            Instant time = now.plus(1, ChronoUnit.DAYS);
            v.setAccessTime(Date.from(time));
        }

        String userId = params.get("userId");
        if (userId == null || userId.isEmpty())
            throw new IllegalArgumentException("Không tìm thấy người đăng ký");
        User user = userRepo.getUserById(userId);
        v.setUserId(user);

        v.setCreatedDate(new Date());
        v.setStatus("Pending");
        v.setIsActive(true);

        return vehicleAccessRepo.addVehicleAccess(v);
    }


}

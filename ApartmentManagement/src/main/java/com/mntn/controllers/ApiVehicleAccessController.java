package com.mntn.controllers;

import com.mntn.pagination.PaginatedResponse;
import com.mntn.pojo.User;
import com.mntn.pojo.VehicleAccess;
import com.mntn.services.VehicleAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiVehicleAccessController {
    @Autowired
    private VehicleAccessService vehicleAccessService;

    @GetMapping("/secure/vehicle")
    public ResponseEntity<PaginatedResponse<VehicleAccess>> getListVehicleAccess(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.vehicleAccessService.getListVehicleAccess(params), HttpStatus.OK);
    }

    @GetMapping("/secure/vehicle/{userId}")
    public ResponseEntity<List<VehicleAccess>> getListVehicleAccessByUserId(@PathVariable(value = "userId") String userId) {
        return new ResponseEntity<>(this.vehicleAccessService.getListVehicleAccessByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/secure/vehicle")
    public ResponseEntity<Object> addVehicleAccess(@RequestBody Map<String, String> params) {
        try {
            return new ResponseEntity<>(this.vehicleAccessService.addVehicleAccess(params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PatchMapping("/secure/vehicle/{id}")
    public ResponseEntity<VehicleAccess> update(@PathVariable(value = "id") String id,
                                                @RequestBody Map<String, String> updates) {
        return new ResponseEntity<>(this.vehicleAccessService.updateVehicleAccess(id, updates), HttpStatus.OK);
    }
}

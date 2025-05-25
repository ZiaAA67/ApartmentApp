package com.mntn.controllers;

import com.mntn.pojo.Apartment;
import com.mntn.pojo.VehicleAccess;
import com.mntn.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiApartmentController {
    @Autowired
    private ApartmentService aptService;

    @GetMapping("/secure/apartment/block/{block}")
    public ResponseEntity<List<Apartment>> getListApartmentByBlock(@PathVariable(value = "block") String block) {
        return new ResponseEntity<>(this.aptService.getListApartmentByBlock(block), HttpStatus.OK);
    }

    @GetMapping("/secure/user/{userId}/apartment")
    public ResponseEntity<List<Apartment>> getListApartmentByUserId(@PathVariable(value = "userId") String userId) {
        return new ResponseEntity<>(this.aptService.getListApartmentByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/secure/apartment/{aptId}")
    public ResponseEntity<Apartment> getApartment(@PathVariable(value = "aptId") String id) {
        return new ResponseEntity<>(this.aptService.getApartmentById(id), HttpStatus.OK);
    }

    @PostMapping("/secure/apartment")
    public ResponseEntity<Object> addApartment(@RequestBody Map<String, String> params) {
        try {
            return new ResponseEntity<>(this.aptService.addApartment(params), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/secure/apartment/{aptId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApartment(@PathVariable(value = "aptId") String id) {
        this.aptService.deleteApartment(id);
    }

    @PatchMapping("/secure/apartment/{aptId}")
    public ResponseEntity<Apartment> update(@PathVariable(value = "aptId") String id,
                                                @RequestBody Map<String, String> updates) {
        return new ResponseEntity<>(this.aptService.updateApartment(id, updates), HttpStatus.OK);
    }
}

package com.mntn.services;

import com.mntn.pojo.Apartment;

import java.util.List;
import java.util.Map;

public interface ApartmentService {
    List<Apartment> getListApartmentByUserId(String userId);
    List<Apartment> getListApartmentByBlock(String block);
    Apartment getApartmentById(String id);
    Apartment addApartment(Map<String, String> params);
    void deleteApartment(String id);
    Apartment updateApartment(String id, Map<String, String> updates);
}

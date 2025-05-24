package com.mntn.repositories;

import com.mntn.pojo.Apartment;

import java.util.List;

public interface ApartmentRepository {
    List<Apartment> getListApartmentByUserId(String userId);
    List<Apartment> getListApartmentByBlock(String block);
    boolean checkExistApartment(String block, String floor, String number);
    Apartment addApartment(Apartment a);
    Apartment updateApartment(Apartment a);
    void deleteApartment(String id);
    Apartment getApartmentById(String id);
}

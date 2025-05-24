package com.mntn.services.impl;

import com.mntn.pojo.Apartment;
import com.mntn.pojo.User;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.ApartmentService;
import com.mntn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Autowired
    ApartmentRepository apartmentRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public List<Apartment> getListApartmentByUserId(String userId) {
        return this.apartmentRepo.getListApartmentByUserId(userId);
    }

    @Override
    public List<Apartment> getListApartmentByBlock(String block) {
        return this.apartmentRepo.getListApartmentByBlock(block);
    }

    @Override
    public Apartment getApartmentById(String id) {
        return this.apartmentRepo.getApartmentById(id);
    }

    @Override
    public Apartment addApartment(Map<String, String> params) {
        Apartment a = new Apartment();

        a.setId(UUID.randomUUID().toString());
        a.setStatus("vacant");

        String userId = params.getOrDefault("userId", null);
        if(userId != null) {
            User u = this.userRepo.getUserById(userId);
            if(u != null) a.setCurrentOwnerId(u);
            a.setStatus("occupied");
        }

        String block = params.get("block");
        if (block == null)
            throw new IllegalArgumentException("Toà nhà chưa được cung cấp!");
        a.setBlock(block);

        String floor = params.get("floor");
        if (floor == null)
            throw new IllegalArgumentException("Số tầng chưa được cung cấp!");
        a.setFloor(floor);

        String number = params.get("number");
        if (number == null)
            throw new IllegalArgumentException("Số nhà chưa được cung cấp!");
        a.setNumber(number);

        if(this.apartmentRepo.checkExistApartment(block, floor, number))
            throw new IllegalArgumentException("Căn hộ đã tồn tại!");

        String area = params.get("area");
        if (area == null)
            throw new IllegalArgumentException("Diện tích chưa được cung cấp!");
        try {
            a.setArea(new BigDecimal(area));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Diện tích không hợp lệ!");
        }

        a.setBathroom(Integer.valueOf(params.getOrDefault("bathroom", "1")));
        a.setBedroom(Integer.valueOf(params.getOrDefault("bedroom", "1")));
        a.setIsActive(true);

        return this.apartmentRepo.addApartment(a);
    }

    @Override
    public void deleteApartment(String id) {
        this.apartmentRepo.deleteApartment(id);
    }

    @Override
    public Apartment updateApartment(String id, Map<String, String> updates) {
        Apartment a = this.apartmentRepo.getApartmentById(id);

        if (updates != null && a != null) {
            updates.forEach((field, value) -> {
                switch (field) {
                    case "userId" ->  {
                        if(value == null) {
                            a.setCurrentOwnerId(null);
                            a.setStatus("vacant");
                        } else {
                            User u = userRepo.getUserById(value);
                            if(u != null) {
                                a.setCurrentOwnerId(u);
                                a.setStatus("occupied");
                            }
                        }
                    }
                }
            });
        }

        return this.apartmentRepo.updateApartment(a);
    }
}

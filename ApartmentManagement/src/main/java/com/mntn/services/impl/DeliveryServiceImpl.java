package com.mntn.services.impl;

import com.mntn.pojo.Apartment;
import com.mntn.pojo.Delivery;
import com.mntn.pojo.User;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.repositories.DeliveryRepository;
import com.mntn.repositories.UserRepository;
import com.mntn.services.DeliveryService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("deliveryService")
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    private ApartmentRepository apartmentRepo;

    @Override
    public List<Delivery> getDelivery(Map<String, String> params) {
        String apartmentId = params.get("apartmentId");
        Apartment a = apartmentRepo.getApartmentById(apartmentId);
        String userId = params.get("userId");
        params.put("status", "waiting");
        if (userId.equals(a.getCurrentOwnerId().getId())) {
            return deliveryRepository.getDelivery(params);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Delivery> getDeliveryAdmin(Map<String, String> params) {
        return deliveryRepository.getDelivery(params);
    }

    @Override
    public Delivery createDelivery(Map<String, String> params) {
        String apartmentId = params.get("apartmentId");
        Apartment a = apartmentRepo.getApartmentById(apartmentId);

        if (a == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy căn hộ với ID đã cho");
        }

        String recipientName = params.get("recipientName");
        String packageDescription = params.get("packageDescription");
        String notes = params.get("notes");
        String id = UUID.randomUUID().toString();

        Delivery d = new Delivery();
        d.setId(id);
        d.setApartmentId(a);
        d.setRecipientName(recipientName);
        d.setPackageDescription(packageDescription);
        d.setArrivedAt(new Date());
        d.setDeliveredAt(null);
        d.setStatus("waiting");
        d.setNotes(notes);

        return this.deliveryRepository.createDelivery(d);
    }

    @Override
    public Delivery updateStatusDelivery(String deliveryId, String status) {
        if (deliveryId == null || status == null) {
            throw new IllegalArgumentException("Delivery ID hoặc Status không được null!");
        }

        Delivery d = deliveryRepository.getDeliveryById(deliveryId);
        if (d == null) {
            throw new IllegalArgumentException("Không tìm thấy Delivery với ID: " + deliveryId);
        }

        d.setStatus(status);
        d.setDeliveredAt(new Date());
        return this.deliveryRepository.updateDelivery(d);
    }

}

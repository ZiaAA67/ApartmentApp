package com.mntn.services;

import com.mntn.pojo.Delivery;
import java.util.List;
import java.util.Map;

public interface DeliveryService {

    List<Delivery> getDelivery(Map<String, String> params);

    List<Delivery> getDeliveryAdmin(Map<String, String> params);

    Delivery createDelivery(Map<String, String> params);

    Delivery updateStatusDelivery(String deliveryId, String status);
}

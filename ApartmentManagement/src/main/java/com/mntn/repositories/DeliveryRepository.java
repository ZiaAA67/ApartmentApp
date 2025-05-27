package com.mntn.repositories;

import com.mntn.pojo.Delivery;
import java.util.List;
import java.util.Map;

public interface DeliveryRepository {

    List<Delivery> getDelivery(Map<String, String> params);

    Delivery getDeliveryById(String deliveryId);

    Delivery createDelivery(Delivery d);

    Delivery updateDelivery(Delivery d);
}

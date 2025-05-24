package com.mntn.repositories;

import com.mntn.pojo.ApartmentImage;

import java.util.List;

public interface ApartmentImageRepository {
    ApartmentImage addImage(ApartmentImage img);
    List<ApartmentImage> getListImages(String aptId);
}

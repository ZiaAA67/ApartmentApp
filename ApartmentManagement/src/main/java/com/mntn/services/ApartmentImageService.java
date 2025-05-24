package com.mntn.services;

import com.mntn.pojo.ApartmentImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentImageService {
    ApartmentImage addImage(String aptId, MultipartFile image);
    List<ApartmentImage> getListImages(String aptId);
}

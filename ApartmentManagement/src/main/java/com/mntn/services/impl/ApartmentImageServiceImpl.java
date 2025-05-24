package com.mntn.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mntn.pojo.Apartment;
import com.mntn.pojo.ApartmentImage;
import com.mntn.pojo.User;
import com.mntn.repositories.ApartmentImageRepository;
import com.mntn.repositories.ApartmentRepository;
import com.mntn.services.ApartmentImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ApartmentImageServiceImpl implements ApartmentImageService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ApartmentImageRepository imageRepo;

    @Override
    public ApartmentImage addImage(String aptId, MultipartFile image) {
        ApartmentImage img = new ApartmentImage();

        img.setId(UUID.randomUUID().toString());
        img.setApartmentId(aptId);
        img.setCreatedDate(new Date());

        if (image != null && !image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                img.setImageUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.imageRepo.addImage(img);
    }

    @Override
    public List<ApartmentImage> getListImages(String aptId) {
        return this.imageRepo.getListImages(aptId);
    }
}

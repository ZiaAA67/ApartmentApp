package com.mntn.controllers;

import com.mntn.pojo.ApartmentImage;
import com.mntn.services.ApartmentImageService;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiApartmentImageController {
    @Autowired
    private ApartmentImageService imageService;

    @PostMapping(path = "/secure/apartment/{aptId}/images", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<Object> addImage(@PathVariable(value = "aptId") String aptId,
                                           @RequestParam(value = "image") MultipartFile image) {
        try {
            return new ResponseEntity<>(this.imageService.addImage(aptId, image), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/secure/apartment/{aptId}/images")
    public ResponseEntity<List<ApartmentImage>> getListVehicleAccess(@PathVariable(value = "aptId") String aptId) {
        return new ResponseEntity<>(this.imageService.getListImages(aptId), HttpStatus.OK);
    }
}

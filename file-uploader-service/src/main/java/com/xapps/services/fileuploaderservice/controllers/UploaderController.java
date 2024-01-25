package com.xapps.services.fileuploaderservice.controllers;

import com.xapps.services.fileuploaderservice.services.UploaderService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploader")
public class UploaderController {

    public static final Logger logger = LoggerFactory.getLogger(UploaderController.class);

    private final UploaderService uploaderService;

    @Autowired
    public UploaderController(UploaderService uploaderService) {
        this.uploaderService = uploaderService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadFile(
        @RequestPart("file") @NotNull MultipartFile file
    ) {
        logger.info("Resquesting uploading file {}...", file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            uploaderService.uploadFile(file);
            return ResponseEntity.ok().build();
        }

    }

}

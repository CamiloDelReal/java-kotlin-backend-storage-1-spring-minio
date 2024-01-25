package com.xapps.services.fileuploaderservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class UploaderControllerAdvice {

    public static final Logger logger = LoggerFactory.getLogger(UploaderControllerAdvice.class);

    @ExceptionHandler(MultipartException.class)
    public void handleException(Exception e) {
        logger.error("Error uploading file: {}", e.getMessage());
    }

}

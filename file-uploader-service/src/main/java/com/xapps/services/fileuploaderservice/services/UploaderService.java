package com.xapps.services.fileuploaderservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {
    void uploadFile(MultipartFile file);
}

package org.devjeans.sid.global.external.imageUpload.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.global.external.imageUpload.dto.PresignedRequest;
import org.devjeans.sid.global.external.imageUpload.service.FileService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ImageUploadController {
    private final FileService fileService;
    @PostMapping("/api/upload/prisigned-url")
    public String getPresignedUrl(@RequestBody PresignedRequest presignedRequest) {
        return fileService.getPreSignedUrl(presignedRequest.getPrefix(), presignedRequest.getUrl());
    }

}

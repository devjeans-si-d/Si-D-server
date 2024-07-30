package org.devjeans.sid.global.external.imageUpload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresignedRequest {
    private String prefix;
    private String url;
}

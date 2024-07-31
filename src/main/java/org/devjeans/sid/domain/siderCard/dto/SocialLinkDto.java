package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLinkDto {
    private String email;
    private String github;
    private String behance;
    private String linkedin;
    private String etc;
}
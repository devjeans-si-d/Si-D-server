package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCardResDto {
    private Long id;
    private String nickname;
    private String domain;
    private String introduction;
    private String image;
    private SocialLinkResDto socialLinkRes;
    private List<CareerResDto> careerRes;
    private List<TeckStackResDto> teckStackRes;
}

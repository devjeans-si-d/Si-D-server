package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCardUpdateReqDto {
    private Long id;
    private JobField jobField;
    private String introduction;
    private String image;
    private SocialLinkDto socialLink;
    private List<CareerReqDto> careers;
    private List<TeckStackReqDto> teckStacks;
}

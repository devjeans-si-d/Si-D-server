package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiDCardListDto {
    private Long member_id;
    private String member_nickname;
    private JobField member_jobField;
    private String member_image;
}

package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.JobField;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeckStackResDto {
    private Long id;
    private JobField jobField;
    private String techStackName;
}

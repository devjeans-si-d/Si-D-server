package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.EmployedYn;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerReqDto {
    private String company;
    private String position;
    private LocalDate employedStart;
    private LocalDate employedEnd;
    private EmployedYn employedYn;
}

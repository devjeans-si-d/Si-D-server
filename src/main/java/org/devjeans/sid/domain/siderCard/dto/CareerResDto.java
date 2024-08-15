package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.EmployedYn;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerResDto {
    private String company;
    private String position;
    private YearMonth employedStart;
    private YearMonth employedEnd;
    private Boolean employedYn;
}

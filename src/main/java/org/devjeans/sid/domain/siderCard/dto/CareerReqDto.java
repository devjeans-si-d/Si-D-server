package org.devjeans.sid.domain.siderCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.Career;
import org.devjeans.sid.domain.siderCard.entity.EmployedYn;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerReqDto {
    private String company;
    private String position;
    private String employedStart;
    private String employedEnd;
    private Boolean employedYn;

    public Career toEntity(SiderCard siderCard) {
        return Career.builder()
                .company(this.company)
                .position(this.position)
                .employedYn(this.employedYn)
                .employedStart(YearMonth.parse(this.employedStart))
                .employedEnd(YearMonth.parse(this.employedEnd))
                .siderCard(siderCard)
                .build();
    }
}

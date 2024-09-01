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
import java.time.format.DateTimeParseException;

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
//        YearMonth ym;
//        try{
//            ym = YearMonth.parse(this.employedEnd);
//        }catch (DateTimeParseException e){
//            ym = null;
//        }catch (NullPointerException e){
//            ym = null;
//        }
        return Career.builder()
                .company(this.company)
                .position(this.position)
                .employedYn(this.employedYn)
                .employedStart(this.employedStart)
                .employedEnd(this.employedEnd)
                .siderCard(siderCard)
                .build();
    }
}

package org.devjeans.sid.domain.siderCard.entity;

import lombok.*;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.dto.CareerResDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Career{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private Boolean employedYn;

    @Column(nullable = false)
    private YearMonth employedStart;

    private YearMonth employedEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sider_card_id")
    private SiderCard siderCard;

    public CareerResDto fromEntity() {
        return CareerResDto.builder()
                .company(this.company)
                .position(this.position)
                .employedStart(this.employedStart)
                .employedEnd(this.employedEnd)
                .employedYn(this.employedYn)
                .build();
    }
}

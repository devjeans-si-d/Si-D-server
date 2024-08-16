package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.dto.TeckStackResDto;


import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiderCardTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sider_card_tech_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tech_stack_id")
    private TechStack techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sider_card_id")
    private SiderCard siderCard;


    public TeckStackResDto fromEntity() {
        return TeckStackResDto.builder()
                .id(techStack.getId())
                .jobField(techStack.getJobField().getJobName())
                .techStackName(techStack.getTechStackName())
                .build();
    }
}

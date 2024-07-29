package org.devjeans.sid.domain.siderCard.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.dto.TeckStackResDto;


import javax.persistence.*;

@Entity
public class SiderCardTechStack extends BaseEntity {
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
                .id(this.id)
                .jobField(techStack.getJobField())
                .techStackName(techStack.getTechStackName())
                .build();
    }
}

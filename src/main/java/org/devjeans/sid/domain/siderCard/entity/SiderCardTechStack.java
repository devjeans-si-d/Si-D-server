package org.devjeans.sid.domain.siderCardTechStack.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.techStack.entity.TechStack;

import javax.persistence.*;

@Entity
public class SiderCardTechStack extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sider_card_tech_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sider_card_id")
    private SiderCard siderCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tech_stack_id")
    private TechStack techStack;
}

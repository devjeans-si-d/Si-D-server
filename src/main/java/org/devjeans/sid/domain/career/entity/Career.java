package org.devjeans.sid.domain.career.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Career extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployedYn employedYn;

    @Column(nullable = false)
    private LocalDateTime employedStart;

    private LocalDateTime getEmployedEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sider_card_id")
    private SiderCard siderCard;
}

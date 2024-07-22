package org.devjeans.sid.domain.project.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.siderCard.entity.JobField;


import javax.persistence.*;

@Entity
public class RecruitInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_info_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobField jobField;

    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}

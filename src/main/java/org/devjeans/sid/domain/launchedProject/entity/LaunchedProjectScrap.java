package org.devjeans.sid.domain.launchedProject.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.member.entity.Member;

import javax.persistence.*;

@Entity
public class LaunchedProjectScrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_scrap_id")
    private Long id; // LaunchedProject - 스크랩 교차테이블 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 스크랩(사이다) 누른 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="launched_project_id")
    private LaunchedProject launchedProject; // 스크랩(사이다) 누른 해당 프로젝트
}

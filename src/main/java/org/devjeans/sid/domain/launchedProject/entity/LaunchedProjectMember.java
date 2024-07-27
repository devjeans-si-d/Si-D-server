package org.devjeans.sid.domain.launchedProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectMember extends BaseEntity { // LaunchedProject - 회원 교차테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launched_project_member_id")
    private Long id; // LaunchedProject - 회원 교차테이블 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="launched_project_id")
    private LaunchedProject launchedProject; // 프로젝트 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 참여한 회원
}

package org.devjeans.sid.domain.launchedProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LaunchedProject extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launched_project_id")
    private Long id; // 프로젝트 전시(Launched-Project) id

    @Column(nullable = false, length = 2083)
    private String launchedProjectImage; // 프로젝트 사진(기본사진 url)

    @Column(nullable = false, length = 5000)
    private String launchedProjectContents; // Launched-Project 글 내용

    @Column(length = 2083)
    private String siteUrl; // 프로젝트 사이트 링크

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project; // 프로젝트 id(project테이블 FK)

    @Column(columnDefinition = "bigint default 0")
    private Long views; // Launched-Project 조회수

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectTechStack> launchedProjectTechStacks = new ArrayList<>(); // Launched-Project에 사용된 기술스택 리스트

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectMember> launchedProjectMembers = new ArrayList<>(); // Launched-Project 참여회원 리스트

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectScrap> launchedProjectScraps = new ArrayList<>(); // Launched-Project 스크랩(사이다) 리스트
}

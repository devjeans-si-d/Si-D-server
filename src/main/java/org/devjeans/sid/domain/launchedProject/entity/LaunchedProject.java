package org.devjeans.sid.domain.launchedProject.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LaunchedProject extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launched_project_id")
    private Long id;

    @Column(nullable = false, length = 2083)
    private String launchedProjectImage;

    @Column(nullable = false, length = 5000)
    private String launchedProjectContents;

    @Column(length = 2083)
    private String siteUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(columnDefinition = "bigint default 0")
    private Long views;

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectTechStack> launchedProjectTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectMember> launchedProjectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "launchedProject", cascade = CascadeType.ALL)
    private List<LaunchedProjectScrap> launchedProjectScraps = new ArrayList<>();
}

package org.devjeans.sid.domain.launchedProjectTechStack.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.techStack.entity.TechStack;

import javax.persistence.*;

@Entity
public class LaunchedProjectTechStack extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launched_project_tech_stack")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="launched_project_id")
    private LaunchedProject launchedProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tech_stack_id")
    private TechStack techStack;
}

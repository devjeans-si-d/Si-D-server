package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.project.entity.JobField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TechStack extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    private Long id; // 기술스택 id

    @Column(nullable = false)
    private String techStackName; // 기술스택 명

    @Enumerated(EnumType.STRING)
    private JobField jobField; // 직무명

    @OneToMany(mappedBy = "techStack", cascade = CascadeType.REFRESH)
    private List<SiderCardTechStack> siderCardTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "techStack", cascade = CascadeType.REFRESH)
    private List<LaunchedProjectTechStack> launchedProjectTechStacks = new ArrayList<>();

}

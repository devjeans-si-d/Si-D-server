package org.devjeans.sid.domain.siderCard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TechStack{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    private Long id;

    @Column(nullable = false)
    private String techStackName;

    @Enumerated(EnumType.STRING)
    private JobField jobField;

    @OneToMany(mappedBy = "techStack", cascade = CascadeType.REFRESH)
    private List<SiderCardTechStack> siderCardTechStacks = new ArrayList<>();

    @OneToMany(mappedBy = "techStack", cascade = CascadeType.REFRESH)
    private List<LaunchedProjectTechStack> launchedProjectTechStacks = new ArrayList<>();

}

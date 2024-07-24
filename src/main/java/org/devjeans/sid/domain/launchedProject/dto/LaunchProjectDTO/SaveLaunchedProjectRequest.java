package org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.siderCard.entity.TechStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveLaunchedProjectRequest {

    private String launchedProjectContents; // Launched-Project 글 내용
    private String siteUrl; // 프로젝트 사이트 링크
    private List<Long> techStackIds; // 기술 스택 ID 리스트

    // LaunchedProject 엔티티로 변환하는 메소드
    public static LaunchedProject toEntity(SaveLaunchedProjectRequest dto,
                                           String imageUrl,
                                           Project project,
                                           List<TechStack> techStacks) {

        LaunchedProject launchedProject = LaunchedProject.builder()
                .launchedProjectImage(imageUrl)
                .launchedProjectContents(dto.getLaunchedProjectContents())
                .siteUrl(dto.getSiteUrl())
                .project(project)
                .build();

        // LaunchedProjectTechStack 리스트 설정
        if (techStacks != null) {
            techStacks.forEach(techStack -> {
                LaunchedProjectTechStack projectTechStack = LaunchedProjectTechStack.builder()
                        .launchedProject(launchedProject)
                        .techStack(techStack)
                        .build();
                launchedProject.getLaunchedProjectTechStacks().add(projectTechStack);
            });
        }

        return launchedProject;
    }
}

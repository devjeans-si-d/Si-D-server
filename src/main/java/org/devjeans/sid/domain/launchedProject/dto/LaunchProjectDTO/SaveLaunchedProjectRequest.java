package org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.project.entity.Project;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveLaunchedProjectRequest {

    private String launchedProjectImage; // 프로젝트 사진 url

    private String launchedProjectContents; // launchedProject글 내용

    private String siteUrl; // 프로젝트 사이트 링크

    private Long projectId; // FK 프로젝트 id


    // SaveLaunchedProjectRequest(DTO)를 LaunchedProject 엔티티로 build
    public static LaunchedProject toEntity(SaveLaunchedProjectRequest dto, Project project) {
        return LaunchedProject.builder()
                .launchedProjectImage(dto.getLaunchedProjectImage())
                .launchedProjectContents(dto.getLaunchedProjectContents())
                .siteUrl(dto.getSiteUrl())
                .project(project)
                .views(0L) // 기본 조회수 : 0
                .build();
    }
}

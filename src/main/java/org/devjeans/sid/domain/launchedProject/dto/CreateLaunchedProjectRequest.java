package org.devjeans.sid.domain.launchedProject.dto;

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
public class CreateLaunchedProjectRequest {

    private String launchedProjectImage; // 프로젝트 사진 url

    private String launchedProjectContents; // launchedProject글 내용

    private String siteUrl; // 프로젝트 사이트 링크

    public static LaunchedProject toEntity(CreateLaunchedProjectRequest dto, Project project){
        return LaunchedProject.builder()
                .launchedProjectImage(dto.getLaunchedProjectImage())
                .launchedProjectContents(dto.getLaunchedProjectContents())
                .siteUrl(dto.getSiteUrl())
                .project(project)
                .build();
    }

}

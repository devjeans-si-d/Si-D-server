package org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailBasicLaunchedProjectResponse {

    private Long id; // 프로젝트 전시(Launched-Project) id

    private String launchedProjectImage; // 프로젝트 사진(기본사진 url)

    private String launchedProjectContents; // Launched-Project 글 내용

    private String siteUrl; // 프로젝트 사이트 링크

    private Long  projectId ; // 프로젝트 id(project테이블 FK)

    private Long views; // Launched-Project 조회수

    // LaunchProject -> DetailBasicLaunchedProjectResponse(DTO)로 build
    public static DetailBasicLaunchedProjectResponse fromEntity(LaunchedProject launchedProject){
        return DetailBasicLaunchedProjectResponse.builder()
                .id(launchedProject.getId())
                .launchedProjectImage(launchedProject.getLaunchedProjectImage())
                .launchedProjectContents(launchedProject.getLaunchedProjectContents())
                .siteUrl(launchedProject.getSiteUrl())
                .projectId(launchedProject.getProject().getId())
                .views(launchedProject.getViews())
                .build();
    }

}


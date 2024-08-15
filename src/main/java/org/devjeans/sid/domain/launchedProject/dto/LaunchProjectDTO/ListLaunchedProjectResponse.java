package org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListLaunchedProjectResponse {

    private Long id; // 프로젝트 전시(Launched-Project) id

    private String launchedProjectImage; // 프로젝트 사진(기본사진 url)

    private String projectName; // 프로젝트 이름 (LaunchedProject -> Project -> projectName)
    private String imageUrl;
    private String launchedProjectContents; // Launched-Project 글 내용 (30자 까지만 잘라서 출력)

    private Long views; // Launched-Project 조회수

    private int scrapCount; // Launched-Project 스크랩 수 (size() : int)

}

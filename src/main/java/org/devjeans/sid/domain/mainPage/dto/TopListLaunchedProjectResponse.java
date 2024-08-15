package org.devjeans.sid.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopListLaunchedProjectResponse {
    // 완성된프로젝트 글id, 제목, 내용(30자), 기술스택(5개)
    private Long id; // 프로젝트 전시(Launched-Project) id

    private String launchedProjectImage; // 프로젝트 사진(기본사진 url)

    private String projectName; // 프로젝트 이름 (LaunchedProject -> Project -> projectName)

    private String launchedProjectContents; // Launched-Project 글 내용 (30자 까지만 잘라서 출력)

    private Long views; // 조회수

    private Long scraps; // 스크랩 수

    private List<String> techStacks; // 기술스택 명 (5개 한정 출력)

}

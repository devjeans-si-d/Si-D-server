package org.devjeans.sid.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopListProjectResponse {

    private Long id; // 프로젝트 id

    private String projectName; // 프로젝트 제목

    private String description; // 프로젝트 한줄설명
}

package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectScrapResponse {

    private Long memberId; // 스크랩(사이다) 누른 회원 id

    private Long launchedProjectId; // Launched-Project 글 id

}

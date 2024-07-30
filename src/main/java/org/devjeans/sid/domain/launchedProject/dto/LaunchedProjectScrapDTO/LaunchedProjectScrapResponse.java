package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.ToggleStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectScrapResponse {

    private Long launchedProjectId; // Launched-Project 글 id

    private int scrapCount; // 스크랩(사이다) 눌린 개수

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ToggleStatus toggleStatus = ToggleStatus.FALSE; // 버튼 눌린 상태

}

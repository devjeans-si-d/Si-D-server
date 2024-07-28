package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectScrapRequest {

    private Long memberId; // 스크랩(사이다) 누른 회원 id

    private Long launchedProjectId; // Launched-Project 글 id


    public static LaunchedProjectScrap toEntity(LaunchedProjectScrapRequest dto,
                                                Member member,
                                                LaunchedProject launchedProject){
        return LaunchedProjectScrap.builder()
                .member(member)
                .launchedProject(launchedProject)
                .build();
    }

}

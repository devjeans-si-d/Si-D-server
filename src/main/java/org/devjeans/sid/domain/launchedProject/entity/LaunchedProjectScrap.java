package org.devjeans.sid.domain.launchedProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectScrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_scrap_id")
    private Long id; // LaunchedProject - 스크랩 교차테이블 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; // 스크랩(사이다) 누른 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="launched_project_id")
    private LaunchedProject launchedProject; // 스크랩(사이다) 누른 해당 프로젝트

    public static LaunchedProjectScrapResponse scrapResfromEntity(LaunchedProject launchedProject,
                                                                  LaunchedProjectScrap launchedProjectScrap){
        return LaunchedProjectScrapResponse.builder()
                .launchedProjectId(launchedProjectScrap.getLaunchedProject().getId()) // 글 id
                .scrapCount(launchedProject.getLaunchedProjectScraps().size()) // 스크랩 수
//                .toggleStatus(toggleStatus)
                .build();
    }

}

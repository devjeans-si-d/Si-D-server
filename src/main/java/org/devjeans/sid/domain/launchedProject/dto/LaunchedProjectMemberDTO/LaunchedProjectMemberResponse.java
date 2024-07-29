package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.domain.project.entity.ProjectMember;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectMemberResponse {

    private Long id; // LaunchedProject - 회원 교차테이블 id

    private Long memberId; // 회원 id

    private String memberName; // 회원 이름


    // LaunchedProjectMember 엔티티 -> LaunchedProjectMemberResponse (DTO)로 build
    public static LaunchedProjectMemberResponse fromEntity(ProjectMember launchedProjectMember) {
        return LaunchedProjectMemberResponse.builder()
                .id(launchedProjectMember.getId())
                .memberId(launchedProjectMember.getMember().getId()) // launchedProjectMember엔티티 member의 id
                .memberName(launchedProjectMember.getMember().getName()) // launchedProjectMember엔티티 member의 이름
                .build();
    }
}

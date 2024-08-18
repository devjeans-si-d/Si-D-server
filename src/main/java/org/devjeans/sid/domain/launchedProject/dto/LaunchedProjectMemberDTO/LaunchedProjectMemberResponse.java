package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.ProjectMember;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectMemberResponse {

    private Long memberId; // 회원 id (Member -> id)

    private String nickname; // 회원 닉네임 (Member -> nickName)

    private JobField jobField; // 이 프로젝트에서 맡은 직무 (ProjectMember -> jobField)

    private String profileImageUrl; // 프로필 이미지

    // LaunchedProjectMember 엔티티 -> LaunchedProjectMemberResponse (DTO)로 build
    public static LaunchedProjectMemberResponse fromEntity(ProjectMember launchedProjectMember) {
        return LaunchedProjectMemberResponse.builder()
                .memberId(launchedProjectMember.getId())
                .memberId(launchedProjectMember.getMember().getId()) // launchedProjectMember엔티티 member의 id
                .nickname(launchedProjectMember.getMember().getNickname()) // launchedProjectMember엔티티 member의 이름
                .build();
    }
}

package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.JobField;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectMemberResponse {

    private Long memberId; // 회원 id (Member -> id)

    private String nickname; // 회원 닉네임 (Member -> nickName)

    private JobField jobField; // 이 프로젝트에서 맡은 직무 (ProjectMember -> jobField)

}

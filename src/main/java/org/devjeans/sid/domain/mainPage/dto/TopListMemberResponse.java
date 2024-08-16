package org.devjeans.sid.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.JobField;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopListMemberResponse {
    private Long id; // 회원id

    private String nickname; // 닉네임

    private String profileImageUrl; // 프로필 이미지 Url

    private JobField jobField; // 직무

}

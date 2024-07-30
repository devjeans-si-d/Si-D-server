package org.devjeans.sid.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopListMemberResponse {
    private Long id; // 회원id

    private String nickname; // 닉네임

//    private String domain; // 사이더 카드 url 도메인

    private String profileImageUrl; // 프로필 이미지 Url

}

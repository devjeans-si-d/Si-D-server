package org.devjeans.sid.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.entity.SocialType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberRequest {
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Long socialId;

    public Member toEntity() {
        Member member = Member.builder()
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .socialType(SocialType.KAKAO)
                .socialId(this.socialId)
                .phoneNumber(this.phoneNumber)
                .profileImageUrl("https://sejeong-file.s3.ap-northeast-2.amazonaws.com/devjeans-sid/devjeans-spring.png")
                .build();
        return member;
    }
}

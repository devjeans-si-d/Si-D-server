package org.devjeans.sid.domain.member.dto;

import lombok.*;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.entity.SocialType;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.hibernate.sql.Update;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class UpdateMemberResponse {
    private Long memberId;

    private String name;

    private String email;

    private String nickname;

    private SocialType socialType;

    private Long socialId;

    private String phoneNumber;

    private String profileImageUrl;

    public static UpdateMemberResponse fromEntity(Member member) {
        return UpdateMemberResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .phoneNumber(member.getPhoneNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}

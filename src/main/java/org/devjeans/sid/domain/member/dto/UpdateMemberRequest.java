package org.devjeans.sid.domain.member.dto;

import lombok.*;
import org.devjeans.sid.domain.member.entity.SocialType;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class UpdateMemberRequest {
    private String name;

    private String nickname;

    private String phoneNumber;
}

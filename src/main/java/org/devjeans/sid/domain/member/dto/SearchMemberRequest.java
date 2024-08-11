package org.devjeans.sid.domain.member.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SearchMemberRequest {
    private String name;
    private String email;
    private String nickname;
}

package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.ProjectApplication;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantResponse {
    private String name;
    private String jobField;
    private String profileImageUrl;
    private String status;

    public static ApplicantResponse fromEntity(ProjectApplication projectApplication) {

        return ApplicantResponse.builder()
                .name(projectApplication.getMember().getName())
                .profileImageUrl(projectApplication.getMember().getProfileImageUrl())
                .jobField(projectApplication.getJobField().toString()) // FIXME: 한글로 수정 필요
                .status(projectApplication.getIsAccepted() ? "승인 완료" : "승인 대기")
                .build();
    }
}

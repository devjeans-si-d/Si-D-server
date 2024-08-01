package org.devjeans.sid.domain.project.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectApplication;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyProjectRequest {
    private JobField jobField;
    private String content; // 지원 내용

    public static ProjectApplication toEntity(Long projectId, Long memberId, ApplyProjectRequest applyProjectRequest) {
        return ProjectApplication.builder()
                .projectId(projectId)
                .memberId(memberId)
                .jobField(applyProjectRequest.getJobField())
                .isAccepted(false)
                .build();
    }
}

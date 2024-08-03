package org.devjeans.sid.domain.project.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectApplication;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyProjectRequest {
    private JobField jobField;
    private String content; // 지원 내용

    public static ProjectApplication toEntity(Project project, Member member, ApplyProjectRequest applyProjectRequest) {
        return ProjectApplication.builder()
                .project(project)
                .member(member)
                .jobField(applyProjectRequest.getJobField())
                .isAccepted(false)
                .build();
    }
}

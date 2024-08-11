package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectApplication;
import org.devjeans.sid.domain.project.entity.ProjectMember;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private Long projectId;
    private String projectName;
    private String imageUrl;
    private String description;
    private JobField jobField;
    private String status;

    public static ApplicationResponse fromEntity(ProjectApplication projectApplication) {
        Project project = projectApplication.getProject();

        String status;

        Optional<ProjectMember> applicant = project.getProjectMembers().stream()
                .filter(p -> p.getMember().getId().equals(projectApplication.getMember().getId()))
                .findFirst();

        if(applicant.isPresent()) {
            status = "승인";
        } else if(project.getIsClosed().equals("Y")) {
            status = "마감";
        } else {
            status = "진행 중";
        }

        return ApplicationResponse.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .imageUrl(project.getImageUrl())
                .jobField(projectApplication.getJobField())
                .status(status)
                .description(project.getDescription())
                .description(project.getDescription())
                .build();
    }
}

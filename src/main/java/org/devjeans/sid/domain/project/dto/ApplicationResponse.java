package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectApplication;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private Long projectId;
    private String projectName;
    private JobField jobField;
    private String isClosed;

    public static ApplicationResponse fromEntity(ProjectApplication projectApplication) {
        Project project = projectApplication.getProject();

        return ApplicationResponse.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .jobField(projectApplication.getJobField())
                .isClosed(project.getIsClosed())
                .build();
    }
}

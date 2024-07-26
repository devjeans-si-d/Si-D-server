package org.devjeans.sid.domain.project.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.Project;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectResponse {
    private Long id;
    private String projectName;
    public static CreateProjectResponse fromEntity(Project project){
        return CreateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .build();
    }
}

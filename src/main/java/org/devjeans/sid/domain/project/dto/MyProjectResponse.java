package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectMember;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyProjectResponse {
    private Long projectId;
    private String projectName;
    private String description;

    // Y, N
    private String isPm;
    private String isClosed;
    private String isLaunched;

    public static MyProjectResponse fromEntity(ProjectMember projectMember, String isLaunched) {
        log.info("line 28 {}, {}", projectMember.getProject().getPm().getId(), projectMember.getMember().getId());

        String isPm = projectMember.getProject().getPm().getId().equals(projectMember.getMember().getId()) ? "Y" : "N";
        String isClosed = projectMember.getProject().getIsClosed();

        return MyProjectResponse.builder()
                .projectId(projectMember.getProject().getId())
                .projectName(projectMember.getProject().getProjectName())
                .description(projectMember.getProject().getDescription())
                .isPm(isPm)
                .isClosed(isClosed)
                .isLaunched(isLaunched)
                .build();
    }

}

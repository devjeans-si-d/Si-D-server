package org.devjeans.sid.domain.project.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.create.CreateProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProjectResponse {
    private Long id;
    private String projectName;
    private String description;
    private String projectImage;
    private String recruitmemtContents;

    public static UpdateProjectResponse fromEntity(Project project){
        return UpdateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .projectImage(project.getProjectImage())
                .recruitmemtContents(project.getRecruitmemtContents())
                .build();
    }
}

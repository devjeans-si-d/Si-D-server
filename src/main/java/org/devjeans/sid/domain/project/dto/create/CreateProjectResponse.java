package org.devjeans.sid.domain.project.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectResponse {
    private Long id;
    private String projectName;
    private String description;
    private String projectImage;
    private String recruitmemtContents;
    private LocalDateTime deadline;
    private List<Member> projectMembers;
    private List<RecruitInfo> recruitInfos;
    private List<ChatRoom> chatRooms;
    public static CreateProjectResponse fromEntity(Project project){
        return CreateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .build();
    }
}

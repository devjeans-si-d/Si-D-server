package org.devjeans.sid.domain.project.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.siderCard.entity.JobField;
import org.devjeans.sid.domain.project.entity.ProjectMember;
//import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
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
    private String isClosed;
    private LocalDateTime deadline;
    @Builder.Default
    private List<CreateProjectResponse.ProjectMemberDto> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<CreateProjectResponse.RecruitInfoDto> recruitInfos=new ArrayList<>();
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitInfoDto{
        private Long id;
        private JobField jobField;
        private Integer count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMemberDto{
        // Todo 사이더카드 연결
        private Long id;
        private String memberName;
        private JobField jobField;
    }
//    private List<ChatRoom> chatRooms;
    public static CreateProjectResponse fromEntity(Project project){
        List<RecruitInfo> recruitInfoList = project.getRecruitInfos();
        List<ProjectMember> projectMemberList = project.getProjectMembers();

        List<CreateProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
        for( RecruitInfo recruitInfo :recruitInfoList){
            CreateProjectResponse.RecruitInfoDto recruitInfoDto = CreateProjectResponse.RecruitInfoDto.builder()
                    .id(recruitInfo.getId())
                    .count(recruitInfo.getCount())
                    .jobField(recruitInfo.getJobField())
                    .build();
            recruitInfoDtos.add(recruitInfoDto);
        }

        List<CreateProjectResponse.ProjectMemberDto> projectMemberDtos = new ArrayList<>();
        for(ProjectMember  projectMember :projectMemberList){
            CreateProjectResponse.ProjectMemberDto projectMemberDto = CreateProjectResponse.ProjectMemberDto.builder()
                    .id(project.getId())
                    .memberName(projectMember.getMember().getName())
                    .jobField(projectMember.getJobField())
                    .build();
            projectMemberDtos.add(projectMemberDto);
        }

        return CreateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .projectImage(project.getProjectImage())
                .recruitmemtContents(project.getRecruitmemtContents())
                .deadline(project.getDeadline())
                .projectMembers(projectMemberDtos)
                .recruitInfos(recruitInfoDtos)
                .build();
    }
}

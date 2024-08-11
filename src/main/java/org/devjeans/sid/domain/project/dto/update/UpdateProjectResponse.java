package org.devjeans.sid.domain.project.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.create.CreateProjectResponse;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.project.entity.JobField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.devjeans.sid.domain.project.entity.ProjectMember;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProjectResponse {
    private Long id;
    private String projectName;
    private String description;
    private String isClosed;
    private LocalDateTime deadline;
    private String recruitmentContents;
    @Builder.Default
    private List<UpdateProjectResponse.ProjectMemberDto> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<UpdateProjectResponse.RecruitInfoDto> recruitInfos=new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitInfoDto{
        private Long id;
        @Enumerated(EnumType.STRING)
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
        @Enumerated(EnumType.STRING)
        private JobField jobField;
    }
    public static UpdateProjectResponse fromEntity(Project project){
        List<RecruitInfo> recruitInfoList = project.getRecruitInfos();
        List<ProjectMember> projectMemberList = project.getProjectMembers();

        List<UpdateProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
        for( RecruitInfo recruitInfo :recruitInfoList){
            UpdateProjectResponse.RecruitInfoDto recruitInfoDto = UpdateProjectResponse.RecruitInfoDto.builder()
                    .id(recruitInfo.getId())
                    .count(recruitInfo.getCount())
                    .jobField(recruitInfo.getJobField())
                    .build();
            recruitInfoDtos.add(recruitInfoDto);
        }

        // projectMember 조립
        List<UpdateProjectResponse.ProjectMemberDto> projectMemberDtos = new ArrayList<>();
        for( ProjectMember projectMember :projectMemberList){
            UpdateProjectResponse.ProjectMemberDto projectMemberDto = UpdateProjectResponse.ProjectMemberDto.builder()
                    .id(projectMember.getId())
                    .memberName(projectMember.getMember().getName())
                    .jobField(projectMember.getJobField())
                    .build();
            projectMemberDtos.add(projectMemberDto);
        }
        return UpdateProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .isClosed(project.getIsClosed())
                .deadline(project.getDeadline())
                .description(project.getDescription())
                .recruitmentContents(project.getRecruitmentContents())
                .projectMembers(projectMemberDtos)
                .recruitInfos(recruitInfoDtos)
                .build();
    }
}

package org.devjeans.sid.domain.project.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectScrap;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.siderCard.entity.JobField;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailProjectResponse {
    // project(view) 포함, projectMembers, recruitInfos, projectScrap

    private Long id;

    private String projectName;

    private String description;

    private String projectImage;

    private String recruitmemtContents;

    private String isClosed;

    private LocalDateTime deadline;

    private Long views;

    private Member pm;
    private Integer scrapCount;

    private List<ProjectMemberDto> projectMembers;

    private List<RecruitInfoDto> recruitInfos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitInfoDto{
        private JobField jobField;
        private Integer count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMemberDto{
        // Todo 사이더카드 연결
        private String memberName;
        private JobField jobField;
    }

//    private List<ProjectScrapDto> projectScraps;

//    private List<ChatRoomDto> chatRooms;

    public static DetailProjectResponse fromEntity(Project project){
        List<RecruitInfo> recruitInfoList = project.getRecruitInfos();
        List<ProjectScrap> projectScrapList = project.getProjectScraps();
        List<ChatRoom> chatRoomList=project.getChatRooms();
        List<ProjectMember> projectMemberList = project.getProjectMembers();

        // recruitInfo 조립
        List<DetailProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
        for( RecruitInfo recruitInfo :recruitInfoList){
            DetailProjectResponse.RecruitInfoDto recruitInfoDto = DetailProjectResponse.RecruitInfoDto.builder()
                    .count(recruitInfo.getCount())
                    .jobField(recruitInfo.getJobField())
                    .build();
            recruitInfoDtos.add(recruitInfoDto);
        }

        // projectMember 조립
        List<DetailProjectResponse.ProjectMemberDto> projectMemberDtos = new ArrayList<>();
        for( ProjectMember projectMember :projectMemberList){
            DetailProjectResponse.ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
                    .memberName(projectMember.getMember().getName())
                    .jobField(projectMember.getJobField())
                    .build();
            projectMemberDtos.add(projectMemberDto);
        }

        return DetailProjectResponse.builder()
                .id(project.getId())
                .pm(project.getPm())
                .scrapCount(projectScrapList.size())
                .projectImage(project.getProjectImage())
                .deadline(project.getDeadline())
                .description(project.getDescription())
                .isClosed(project.getIsClosed())
                .projectName(project.getProjectName())
                .views(project.getViews())
                .recruitInfos(recruitInfoDtos)
                .projectMembers(projectMemberDtos)
                .build();
    }
}

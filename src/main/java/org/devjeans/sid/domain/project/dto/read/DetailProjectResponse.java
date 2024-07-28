package org.devjeans.sid.domain.project.dto.read;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    // Todo Ignore 안 쓰면 detail 17번 불렀을 때 에러남!! 강사님께 여쭤보기
    private Member pm;
    private Integer scrapCount;
    @Builder.Default
    private List<ProjectMemberDto> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<RecruitInfoDto> recruitInfos=new ArrayList<>();

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
            DetailProjectResponse.RecruitInfoDto recruitInfoDto = RecruitInfoDto.builder()
                    .id(recruitInfo.getId())
                    .count(recruitInfo.getCount())
                    .jobField(recruitInfo.getJobField())
                    .build();
            recruitInfoDtos.add(recruitInfoDto);
        }

        // projectMember 조립
        List<DetailProjectResponse.ProjectMemberDto> projectMemberDtos = new ArrayList<>();
        for( ProjectMember projectMember :projectMemberList){
            DetailProjectResponse.ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
                    .id(project.getId())
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
                .recruitmemtContents(project.getRecruitmemtContents())
                .projectMembers(projectMemberDtos)
                .build();
    }
}

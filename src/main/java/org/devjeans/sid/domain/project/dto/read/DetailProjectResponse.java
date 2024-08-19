package org.devjeans.sid.domain.project.dto.read;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.ApplicantsCountResDto;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectScrap;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
//import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private String imageUrl;
    private String projectName;
    private boolean isScrap;
    private String description;
    private LocalDateTime createdAt;

    private String recruitmentContents;

    private String isClosed;

    private LocalDateTime deadline;

    private Long views;
    private Long scrapCount;

    private String pmName;
    private String pmEmail;
    private String pmNickname;
    private String pmImage;
    private Long pmId;
    @Builder.Default
    private List<ProjectMemberDto> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<RecruitInfoDto> recruitInfos=new ArrayList<>();

    private ApplicantsCountResDto applicantsCount;

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
        private String memberNickname;
        private Long memberId;
        private String memberEmail;
        private String memberImageUrl;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
    }

//    private List<ProjectScrapDto> projectScraps;

//    private List<ChatRoomDto> chatRooms;

    public static DetailProjectResponse fromEntity(Project project, Long scrapCount, Long views, boolean isScrap, ApplicantsCountResDto dto){
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
                    .id(projectMember.getId())
                    .memberName(projectMember.getMember().getName())
                    .memberId(projectMember.getMember().getId())
                    .memberNickname(projectMember.getMember().getNickname())
                    .memberEmail(projectMember.getMember().getEmail())
                    .memberImageUrl(projectMember.getMember().getProfileImageUrl())
                    .jobField(projectMember.getJobField())
                    .build();
            projectMemberDtos.add(projectMemberDto);
        }

        return DetailProjectResponse.builder()
                .id(project.getId())
                .isScrap(isScrap)
                .imageUrl(project.getImageUrl())
                .pmId(project.getPm().getId())
                .pmName(project.getPm().getName())
                .pmEmail(project.getPm().getEmail())
                .createdAt(project.getCreatedAt())
                .pmNickname(project.getPm().getNickname())
                .pmImage(project.getPm().getProfileImageUrl())
                .scrapCount(scrapCount)
                .deadline(project.getDeadline())
                .description(project.getDescription())
                .isClosed(project.getIsClosed())
                .projectName(project.getProjectName())
                .views(views)
                .recruitInfos(recruitInfoDtos)
                .recruitmentContents(project.getRecruitmentContents())
                .projectMembers(projectMemberDtos)
                .applicantsCount(dto)
                .build();
    }
}

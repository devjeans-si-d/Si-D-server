package org.devjeans.sid.domain.project.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.siderCard.entity.JobField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {
    private Long pmId;
    private String projectName;
    private String description;
    private String projectImage;
    private String recruitmemtContents;
    private LocalDateTime deadline;
    @Builder.Default
    private List<ProjectMemberCreateRequest> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<RecruitInfoCreateRequest> recruitInfos=new ArrayList<>();
//    private List<ProjectScrap> projectScraps = new ArrayList<>();
//    private List<ChatRoomCreateRequest> chatRooms;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectMemberCreateRequest{
        private Long memberId;
        private JobField jobField;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecruitInfoCreateRequest{
        private JobField jobField;
        private Integer count;
    }


    public Project toEntity(Member member){
        return Project.builder()
                .pm(member)
                .projectName(this.projectName)
                .description(this.description)
                .projectImage(this.projectImage)
                .recruitmemtContents(this.recruitmemtContents)
                .isClosed("N")
                .deadline(this.deadline)
                .views(0L)
                .build();
    }



}

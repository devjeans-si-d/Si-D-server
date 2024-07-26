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
    private List<Member> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<RecruitInfo> recruitInfos=new ArrayList<>();
//    private List<ProjectScrap> projectScraps = new ArrayList<>();
    @Builder.Default
    private List<ChatRoom> chatRooms=new ArrayList<>();

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

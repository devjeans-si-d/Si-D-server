package org.devjeans.sid.domain.project.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.project.entity.JobField;
//import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.project.entity.ProjectMember;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {
//    private Long pmId; // pathvariable id !=pmId => error 처리
    private String projectName;
    private String description;
    private String recruitmemtContents;
    private String isClosed;
    private LocalDateTime deadline;
    @Builder.Default
    private List<ProjectMemberUpdateRequest> projectMembers=new ArrayList<>();
    @Builder.Default
    private List<RecruitInfoUpdateRequest> recruitInfos=new ArrayList<>();
//    @Builder.Default
//    private List<RecruitInfo> recruitInfos = new ArrayList<>();

//    @Builder.Default
//    private List<ProjectMemberAddRequest> addProjectMembers=new ArrayList<>();
//    @Builder.Default
//    private List<ProjectMemberDeleteRequest> deleteProjectMembers=new ArrayList<>();
//    @Builder.Default
//    private List<RecruitInfoAddRequest> addRecruitInfos=new ArrayList<>();
//    @Builder.Default
//    private List<RecruitInfoDeleteRequest> deleteRecruitInfos=new ArrayList<>();
//    private List<ProjectScrap> projectScraps = new ArrayList<>();
//    private List<ChatRoomCreateRequest> chatRooms;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectMemberUpdateRequest{
        private Long memberId;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecruitInfoUpdateRequest{
        @Enumerated(EnumType.STRING)
        private JobField jobField;
        private Integer count;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectMemberDeleteRequest{
        private Long id;
        private Long memberId;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectMemberAddRequest{
        private Long memberId;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecruitInfoDeleteRequest{
        private Long id;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
        private Integer count;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecruitInfoAddRequest{
        private Long id;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
        private Integer count;
    }


    public static Project updateProject(Project project, UpdateProjectRequest dto){
         // Todo : pm은 수정 불가능한건지 팀원과 체크 필요
        project.setProjectName(dto.getProjectName());
        project.setDeadline(dto.getDeadline());
        project.setIsClosed(dto.getIsClosed());
        project.setDescription(dto.getDescription());
        project.setRecruitmemtContents(dto.getRecruitmemtContents());
        return project;

    }
}

package org.devjeans.sid.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    private String projectName;

    @Column(nullable = false)
    private String description;


    @Column(nullable = false, length = 5000)
    private String recruitmemtContents;

    @Column(columnDefinition = "varchar(1) default 'N'")
    private String isClosed;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @ColumnDefault("0")
    private Long views=0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pm_id")
    @JsonIgnore
    private Member pm;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<RecruitInfo> recruitInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<ProjectScrap> projectScraps = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public void updateNewProjectMembers(List<ProjectMember> newProjectMembers){
        // 기존 리스트 삭제
//        this.projectMembers.clear();
        // 새로운 리스트 추가
//        if (newProjectMembers != null) {
            this.projectMembers.addAll(newProjectMembers);
//        }
    }
    public void updateRecruitInfos(List<RecruitInfo> recruitInfos){
        this.recruitInfos = recruitInfos;
    }

    public void updateIsClosed(String yn){
        this.isClosed=yn;
    }

}

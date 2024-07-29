package org.devjeans.sid.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.member.entity.Member;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    private String projectName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 2083)
    private String projectImage;

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
    private Member pm;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<RecruitInfo> recruitInfos = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectScrap> projectScraps = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public void updateProjectMembers(List<ProjectMember> newProjectMembers){
        // 기존 리스트 삭제
        this.projectMembers.clear();
        // 새로운 리스트 추가
        if (newProjectMembers != null) {
            this.projectMembers.addAll(newProjectMembers);
        }
    }
}

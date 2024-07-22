package org.devjeans.sid.domain.member.entity;

import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProjectScrap.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.projectScrap.entity.ProjectScrap;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable=false)
    private Long socialId;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(mappedBy = "member",cascade = CascadeType.ALL)
    private SiderCard siderCard;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
//    private List<LaunchedProjectMember> launchedProjectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ProjectScrap> projectScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<LaunchedProjectScrap> launchedProjectScraps = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<ChatParticipant> chatParticipants = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessages = new ArrayList<>();

}

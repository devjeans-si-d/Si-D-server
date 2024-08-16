package org.devjeans.sid.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.mainPage.dto.TopListMemberResponse;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;

import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;


import javax.persistence.*;

@ToString
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable=false)
    private Long socialId;

    @Column(nullable = false)
    private String phoneNumber;

    private String profileImageUrl;

//    트랜잭션으로 회원가입할때 사이더카드도 같이 생성하여 id 공유
//    @OneToOne(mappedBy = "member",cascade = CascadeType.ALL)
//    private SiderCard siderCard;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
//    private List<LaunchedProjectMember> launchedProjectMembers = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
//    private List<ProjectMember> projectMembers = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<ProjectScrap> projectScraps = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<LaunchedProjectScrap> launchedProjectScraps = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<ChatParticipant> chatParticipants = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessages = new ArrayList<>();

    //== Custom methos ==//

    public void updateProfileImageUrl(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void updateMemberInfo(UpdateMemberRequest updateMemberRequest) {
        this.name = updateMemberRequest.getName();
        this.nickname = updateMemberRequest.getNickname();
        this.phoneNumber = updateMemberRequest.getPhoneNumber();
    }
    public void deleteMember(){
        this.updateDeleteAt();
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public static TopListMemberResponse topListResFromMember(Member member,
                                                             SiderCard siderCard){

        return TopListMemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(siderCard.getImage())
                .jobField(siderCard.getJobField())
                .build();
    }

}

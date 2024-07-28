package org.devjeans.sid.domain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.common.BaseEntity;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.siderCard.entity.JobField;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProjectMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private JobField jobField; // 이 프로젝트에서 맡은직무

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;

    // LaunchedProject - Service에서 사용
    public static LaunchedProjectMemberResponse memberResfromEntity (ProjectMember member){
        return LaunchedProjectMemberResponse.builder()
                .memberId(member.getMember().getId()) // Member의 id
                .nickname(member.getMember().getNickname()) // Member의 닉네임
                .jobField(member.getJobField()) // 이 프로젝트에서 맡은 직무
                .build();
    }

}

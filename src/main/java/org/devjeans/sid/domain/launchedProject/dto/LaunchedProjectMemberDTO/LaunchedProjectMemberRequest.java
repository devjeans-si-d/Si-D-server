package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;
import org.devjeans.sid.domain.siderCard.entity.JobField;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectMemberRequest {

    private Long id; // member의 id

    private JobField jobField; // 이 프로젝트에서 맡은 직무

    public static ProjectMember toEntity(LaunchedProjectMemberRequest dto,
                                         Member member,
                                         Project project
                                         ){
        return ProjectMember.builder()
                .member(member)
                .jobField(dto.jobField)
                .project(project)
                .build();
    }

}

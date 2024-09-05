package org.devjeans.sid.domain.project.dto.scrap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectScrap;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScrapResponse {
    private Long memberId;
    private Long projectId;
    private Long scrapCount;


    public static ProjectScrap toEntity(Project project, Member member){
        return ProjectScrap.builder().project(project).member(member).build();
    }
}

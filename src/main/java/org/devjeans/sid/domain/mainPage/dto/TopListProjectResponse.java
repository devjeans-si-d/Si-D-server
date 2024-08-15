package org.devjeans.sid.domain.mainPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopListProjectResponse {

    private Long id; // 프로젝트 id

    private String projectName; // 프로젝트 제목

    private String imageUrl; // 이미지 url

    private String description; // 프로젝트 한줄설명

    private Long views; // 조회수

    private Long scraps; // 스크랩 수

    private List<String> recruitInfos; // 모집정보 (직무명만)

    public static TopListProjectResponse topListResFromEntity (Project project,
                                                               Long views,
                                                               Long scraps
    ){
        // 완성된 프로젝트 글 내용 25자까지만 잘라서 출력
        String description = project.getDescription();
        String truncatedDescription = description != null && description.length() > 25 ? description.substring(0, 25) : description;

        List<String> recruitJobfieldList = new ArrayList<>();
        String jobField = "";
        List<RecruitInfo> recruitInfos = project.getRecruitInfos();

        for(RecruitInfo info : recruitInfos){
            jobField = info.getJobField().toString();
            recruitJobfieldList.add(jobField);
        }

        return TopListProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .imageUrl(project.getImageUrl())
                .description(truncatedDescription)
                .views(views)
                .scraps(scraps)
                .recruitInfos(recruitJobfieldList)
                .build();
    }

}

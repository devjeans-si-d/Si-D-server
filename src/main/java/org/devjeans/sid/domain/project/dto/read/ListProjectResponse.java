package org.devjeans.sid.domain.project.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListProjectResponse {
    private Long id;

    private String imageUrl;

    private String projectName;

    private String description;
//    private Boolean isScrap;

    private String isClosed;

    private LocalDateTime deadline;

    // 추후 인기순 정렬이 필요할 때를 위해..?
    private Long views;
    private Long scrapCount;
    @Builder.Default
    private List<ListProjectResponse.RecruitInfoDto> recruitInfos=new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitInfoDto{
        private Long id;
        @Enumerated(EnumType.STRING)
        private JobField jobField;
        private Integer count;
    }

}

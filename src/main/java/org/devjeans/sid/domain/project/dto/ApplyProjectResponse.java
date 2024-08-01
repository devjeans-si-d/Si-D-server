package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.ProjectApplication;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyProjectResponse {
    private Long memberId;
    private Long applyId;
    private Long projectId;

    public static ApplyProjectResponse fromEntity(ProjectApplication apply) {
        return ApplyProjectResponse.builder()
                .applyId(apply.getId())
                .projectId(apply.getProjectId())
                .memberId(apply.getMemberId())
                .build();
    }
}

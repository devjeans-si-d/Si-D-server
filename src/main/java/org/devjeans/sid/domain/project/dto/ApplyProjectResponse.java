package org.devjeans.sid.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyProjectResponse {
    private Long memberId;
    private Long applyId;
    private Long projectId;
}

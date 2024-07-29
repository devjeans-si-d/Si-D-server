package org.devjeans.sid.domain.project.dto.create.createListRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.siderCard.entity.JobField;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProjectMemberRequest {
    private Long memberId;
    private JobField jobField;
    private Long projectId;
}

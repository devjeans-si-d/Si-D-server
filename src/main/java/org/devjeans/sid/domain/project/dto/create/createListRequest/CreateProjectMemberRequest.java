package org.devjeans.sid.domain.project.dto.create.createListRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProjectMemberRequest {
    private Long memberId;
    @Enumerated(EnumType.STRING)
    private JobField jobField;
    private Long projectId;
}

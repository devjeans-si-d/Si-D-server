package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.project.entity.JobField;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectTechStackResponse {
    private Long id;
    @Enumerated(EnumType.STRING)
    private String jobField; // 직무 명

    private String techStackName; // 기술스택 명

}

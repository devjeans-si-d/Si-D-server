package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.siderCard.entity.JobField;
import org.devjeans.sid.domain.siderCard.entity.TechStack;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectTechStackRequest {

    private Long techStackId; // 기술 스택 ID

    // LaunchedProjectTechStackRequest(DTO) -> LaunchedProjectTechStack 엔티티로 build
    public static LaunchedProjectTechStack toEntity(Long launchedProjectId, Long techStackId) {
        return LaunchedProjectTechStack.builder()
                .launchedProject(LaunchedProject.builder().id(launchedProjectId).build())
                .techStack(TechStack.builder().id(techStackId).build())
                .build();
    }

}

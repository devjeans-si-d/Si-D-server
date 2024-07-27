package org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchedProjectTechStackResponse {

    private Long id; // LaunchedProject - 기술스택 교차테이블 id
    private String techStackName; // 기술스택 명

    // LaunchedProjectTechStack -> LaunchedProjectTechStackResponse(DTO)로 build
    public static LaunchedProjectTechStackResponse fromEntity(LaunchedProjectTechStack launchedProjectTechStack) {
        return LaunchedProjectTechStackResponse.builder()
                .id(launchedProjectTechStack.getId())
                .techStackName(launchedProjectTechStack.getTechStack().getTechStackName())
                .build();
    }

}

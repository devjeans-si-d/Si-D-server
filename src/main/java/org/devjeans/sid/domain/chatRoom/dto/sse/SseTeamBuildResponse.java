package org.devjeans.sid.domain.chatRoom.dto.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseTeamBuildResponse {
    private Long projectId;
    private String projectName;
}

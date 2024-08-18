package org.devjeans.sid.domain.chatRoom.dto.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private String type;
    private Object notification;
    private LocalDateTime time;
}

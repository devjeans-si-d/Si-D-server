package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomMessageResponse {
    private Long sender;
    private String content;
    private LocalDateTime createdAt;

    public static ChatRoomMessageResponse fromEntity(ChatMessage message) {
        return ChatRoomMessageResponse.builder()
                .sender(message.getMember().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

}

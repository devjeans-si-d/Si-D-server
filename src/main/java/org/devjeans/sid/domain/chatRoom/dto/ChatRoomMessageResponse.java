package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;

import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomMessageResponse {
    private Long chatroomId;
    private Long sender;
    private String content;
//    private LocalDateTime createdAt;
//    private String createdAt;

    public static ChatRoomMessageResponse fromEntity(ChatMessage message) {
        return ChatRoomMessageResponse.builder()
                .chatroomId(message.getChatRoom().getId())
                .sender(message.getMember().getId())
                .content(message.getContent())
//                .createdAt(message.getCreatedAt().toString()) // 임시 수정
                .build();
    }

}

package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;

@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageRequest {
    private Long chatroomId;
    private String token;
    private String content;

    public static ChatMessage toEntity(ChatRoom chatRoom, Member sender, boolean isRead, String content) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(content)
                .isRead(isRead)
                .member(sender)
                .build();
    }
}

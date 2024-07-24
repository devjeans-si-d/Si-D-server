package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChatRoomResponse {
    private Long chatRoomId;
    private Long projectId;
    private List<Long> participantIds;

    public static CreateChatRoomResponse fromEntity(ChatRoom chatRoom) {
        List<Long> participantIds = chatRoom.getChatParticipants()
                .stream()
                .map(p -> p.getMember().getId())
                .collect(Collectors.toList());

        return CreateChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .projectId(chatRoom.getProject().getId())
                .participantIds(participantIds)
                .build();
    }
}

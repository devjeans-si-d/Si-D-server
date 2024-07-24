package org.devjeans.sid.domain.chatRoom.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateChatRoomResponse {
    private Long chatRoomId;
    private Long projectId;
    private List<Long> participantIds;
}

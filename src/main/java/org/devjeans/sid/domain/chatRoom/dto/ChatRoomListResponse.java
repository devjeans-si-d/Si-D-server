package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomListResponse {
    private Long totalCount; // 총 채팅방 몇개?
    private int currentPage; // 현재 몇번 페이지?
    private List<ChatRoomSimpleResponse> chatRoomList;
}

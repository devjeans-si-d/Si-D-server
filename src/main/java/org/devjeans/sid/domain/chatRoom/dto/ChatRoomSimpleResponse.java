package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomSimpleResponse { // 밖에서 미리보기처럼 보이는 채팅방 1개의 정보
    private Long chatRoomId;
    private Long projectId;
    private String participantNickName; // 상대방 이름
    private String participantProfile; // 상대방 프로필 사진
    private Long unreadCount; // 읽었는지 여부
    private String unreadContent; // 채팅 내용

    public static ChatRoomSimpleResponse fromEntity(ChatRoom chatRoom, Long unreadCount, Member participant, String unreadContent) {
        return ChatRoomSimpleResponse.builder()
                .chatRoomId(chatRoom.getId())
                .projectId(chatRoom.getProject().getId())
                .participantNickName(participant.getNickname())
                .participantProfile(participant.getProfileImageUrl())
                .unreadCount(unreadCount)
                .unreadContent(unreadContent)
                .build();
    }

}

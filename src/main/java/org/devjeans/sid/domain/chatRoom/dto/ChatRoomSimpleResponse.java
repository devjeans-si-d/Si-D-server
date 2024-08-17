package org.devjeans.sid.domain.chatRoom.dto;

import lombok.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.entity.Project;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomSimpleResponse { // 밖에서 미리보기처럼 보이는 채팅방 1개의 정보
    private Long chatRoomId;
    private Long participantMemberId; // 상대방의 멤버 아이디
    private String participantNickName; // 상대방 이름
    private String participantProfileImageUrl; // 상대방 프로필 사진
    private Long unreadCount; // 읽었는지 여부
    private String unreadContent; // 채팅 내용

    // 프로젝트 정보
    private Long projectId;
    private String projectName;
    private String projectImage;
    private String projectDescription;
    private LocalDateTime deadline;
    private String isClosed;

    public static ChatRoomSimpleResponse fromEntity(ChatRoom chatRoom, Long unreadCount, Member participant, String unreadContent) {
        return ChatRoomSimpleResponse.builder()
                .chatRoomId(chatRoom.getId())
                .projectId(chatRoom.getProject().getId())
                .participantMemberId(participant.getId())
                .participantNickName(participant.getNickname())
                .participantProfileImageUrl(participant.getProfileImageUrl())
                .unreadCount(unreadCount)
                .unreadContent(unreadContent)
                .projectName(chatRoom.getProject().getProjectName())
                .projectImage(chatRoom.getProject().getImageUrl())
                .projectDescription(chatRoom.getProject().getDescription())
                .deadline(chatRoom.getProject().getDeadline())
                .isClosed(chatRoom.getProject().getIsClosed())
                .build();
    }

}

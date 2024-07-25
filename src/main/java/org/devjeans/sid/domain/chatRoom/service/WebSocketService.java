package org.devjeans.sid.domain.chatRoom.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.component.ConnectedMap;
import org.devjeans.sid.domain.chatRoom.dto.ChatMessageRequest;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ChatExceptionType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.NO_RECEIVER;

@RequiredArgsConstructor
@Service
public class WebSocketService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations messagingTemplate; // TODO: 주입이 되나? 확인

    // 웹소켓 커넥션 상태 관리
    private final ConnectedMap connectedMap;

    @Transactional
    public void sendMessage(Long chatRoomId, Long receiverId, ChatMessageRequest chatMessageRequest) {
        // chat room 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);boolean isRead = false;
        // TODO: 인증 구현 후, 상대방 찾기 => findParticipantMemberId


        // 멤버가 현재 접속해있는지를 확인
        Long receiverChatRoomId = connectedMap.getChatroomIdByMemberId(receiverId);
        if(receiverChatRoomId != null && receiverChatRoomId.equals(chatRoomId)) {
            // 접속해있다면 바로 보내준다.
            isRead = true;
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatRoomId, chatMessageRequest);
        }



        // 보낸 사람 찾기
        Member sender = memberRepository.findByIdOrThrow(chatMessageRequest.getSenderId());
        ChatMessage chatMessage = ChatMessageRequest.toEntity(chatRoom, sender, isRead, chatMessageRequest.getContent());
        chatMessageRepository.save(chatMessage); // 메시지를 저장한다.
    }

    private Long findParticipantMemberId(ChatRoom chatRoom) {
        Long memberId = 1L; // FIXME: 인증 구현 후 수정 필요(시큐리티 컨텍스트에서 가져오기)
        return chatRoom.getChatParticipants().stream()
                .filter(p -> p.getMember().getId().equals(memberId))
                .findAny().orElseThrow(() -> new BaseException(NO_RECEIVER))
                .getMember()
                .getId();
    }

}

package org.devjeans.sid.domain.chatRoom.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.controller.ChatRoomKey;
import org.devjeans.sid.domain.chatRoom.dto.ChatMessageRequest;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class WebSocketService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations messagingTemplate; // TODO: 주입이 되나? 확인

    // 웹소켓 커넥션 상태 관리
    private static final Set<ChatRoomKey> connectedSet = new HashSet<>();

    @Transactional
    public void sendMessage(Long chatRoomId, Long receiverId, ChatMessageRequest chatMessageRequest) {
        boolean isUnread = true;
        // 멤버가 현재 접속해있는지를 확인
        if(connectedSet.contains(new ChatRoomKey(receiverId, chatRoomId))) {
            // 접속해있다면 바로 보내준다.
            isUnread = false;
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatRoomId, chatMessageRequest);
//            messagingTemplate.convertAndSendToUser(String.valueOf(chatRoomId), "/queue/reply/" + chatRoomId, chatMessageRequest);
        }

        // chat room 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);

        // 보낸 사람 찾기
        Member sender = memberRepository.findByIdOrThrow(chatMessageRequest.getSenderId());
        ChatMessage chatMessage = ChatMessageRequest.toEntity(chatRoom, sender, isUnread, chatMessageRequest.getContent());
        chatMessageRepository.save(chatMessage); // 메시지를 저장한다.
    }

}

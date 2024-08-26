package org.devjeans.sid.domain.chatRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.component.ConnectedMap;
import org.devjeans.sid.domain.chatRoom.dto.ChatMessageRequest;
import org.devjeans.sid.domain.chatRoom.dto.ChatRoomMessageResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseChatResponse;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatParticipantRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.NO_RECEIVER;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final SseService sseService;

    // 웹소켓 커넥션 상태 관리
    private final ConnectedMap connectedMap;

    @Transactional
    public void sendMessage(Long chatRoomId, Long memberId, ChatMessageRequest chatMessageRequest) {
        // 보낸 사람 찾기
        Member sender = memberRepository.findByIdOrThrow(memberId);

        // chat room 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);
        chatRoom.updateRecentChatTime(LocalDateTime.now());
        boolean isRead = false;


        // 멤버가 현재 접속해있는지를 확인
        // TODO: chatroom에 포함된 모든 멤버 들고 오기
        List<ChatParticipant> memberList = chatParticipantRepository.findAllByChatRoom(chatRoom);

        for (ChatParticipant receiver : memberList) {
            Long receiverId = receiver.getMember().getId();
            if(receiverId.equals(memberId)) {
                continue;
            }

            Long receiverChatRoomId = connectedMap.getChatroomIdByMemberId(receiverId);
            if(receiverChatRoomId != null && receiverChatRoomId.equals(chatRoomId)) {
                // 접속해있다면 읽음 표시
                isRead = true;
            } else {
                //== SSE 로직 => member 닉네임 ==//
                SseChatResponse sseChatResponse = new SseChatResponse(chatRoom.getId(), sender.getNickname(), chatMessageRequest.getContent());
                sseService.sendChatNotification(receiverId, sseChatResponse);
            }
        }

        ChatMessage chatMessage = ChatMessageRequest.toEntity(chatRoom, sender, isRead, chatMessageRequest.getContent());
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);// 메시지를 저장한다.

        ChatRoomMessageResponse chatRoomMessageResponse = ChatRoomMessageResponse.fromEntity(savedMessage);




        messagingTemplate.convertAndSend("/sub/chatroom/" + chatRoomId, chatRoomMessageResponse);
    }

    // 추후 jwt 인증이 구현되면 사용할 수 있는 메서드. receiverId를 받을 필요없이 자신의 아이디로 상대방을 찾을 수 있다.
    private Long findReceiverMemberId(ChatRoom chatRoom, Long memberId) {
        return chatRoom.getChatParticipants().stream()
                .filter(p -> !p.getMember().getId().equals(memberId))
                .findAny().orElseThrow(() -> new BaseException(NO_RECEIVER))
                .getMember()
                .getId();
    }

}

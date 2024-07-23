package org.devjeans.sid.domain.chatRoom.chatService;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.dto.ChatRoomSimpleResponse;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatParticipantRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ChatExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.INVALID_CHATROOM;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MemberRepository memberRepository;

    // 해당 회원이 속한 채팅방을 updatedAt DESC로 정렬해서 보여주기
    public Page<ChatRoomSimpleResponse> getChatRoomList(Pageable pageable, Long memberId) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberIdOrderByUpdatedAtDesc(pageable, memberId);

         // TODO: 로직 잘 맞는지 다시 검증
        return chatRooms.map(r -> {
            ChatParticipant chatParticipant = r.getChatParticipants().stream()
                    .filter(p -> !p.getMember().getId().equals(memberId))
                    .findFirst()
                    .orElseThrow(() -> new BaseException(INVALID_CHATROOM));

            Page<ChatMessage> unreadMessages = chatMessageRepository.findAllByIsReadAndChatRoomIdOrderByUpdatedAt(pageable,false, r.getId());

            ChatMessage recentMessage = null;
            // unread Message가 없을 경우 처리
            if (!unreadMessages.getContent().isEmpty()) {
                unreadMessage = unreadMessages.getContent().get(0);
            }

            return ChatRoomSimpleResponse.fromEntity(r, unreadMessages.getTotalElements(), chatParticipant.getMember(), unreadMessage.getContent());
        });
    }
}

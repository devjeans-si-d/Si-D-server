package org.devjeans.sid.domain.chatRoom.chatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.dto.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatParticipantRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ChatExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    // 해당 회원이 속한 채팅방을 updatedAt DESC로 정렬해서 보여주기
    public Page<ChatRoomSimpleResponse> getChatRoomList(Pageable pageable, Long memberId) {
        // 해당 멤버가 속한 채팅방 아이디 다 뽑아오기
        List<ChatParticipant> participants = chatParticipantRepository.findAllByMemberId(memberId);
        List<Long> chatRoomIds = participants.stream()
                .map(p -> p.getChatRoom().getId())
                .distinct()
                .collect(Collectors.toList());

        // 최신 순 정렬
        Page<ChatRoom> chatRooms = chatRoomRepository.findAllByIds(pageable, chatRoomIds);


        return chatRooms.map(chatRoom -> {

            // 최근 메시지 뽑기
            List<ChatMessage> recentMessages = chatMessageRepository.findRecentMessageByChatRoom(chatRoom);

            if(recentMessages.isEmpty()) {
                throw new BaseException(NO_RECENT_MESSAGE);
            }
            // 상대방 찾기
            Member participant = chatRoom.getChatParticipants().stream()
                    .filter(p -> !p.getMember().getId().equals(memberId))
                    .findFirst()
                    .map(ChatParticipant::getMember)
                    .orElseThrow(() -> new BaseException(INVALID_CHATROOM));

            // 안읽은 메시지 개수 뽑기
            Long unreadCount = chatMessageRepository.countChatMessageByChatRoomAndIsReadAndMember(chatRoom, false, participant);


            return ChatRoomSimpleResponse.fromEntity(chatRoom, unreadCount, participant, recentMessages.get(0).getContent());
        });


    }

    @Transactional
    public Slice<ChatRoomMessageResponse> getChatRoomMessages(Pageable pageable, Long chatRoomId, Long memberId) {
        resolveUnread(chatRoomId, memberId);
        Slice<ChatMessage> messages = chatMessageRepository.findAllByChatRoomId(pageable, chatRoomId);
        return messages.map(ChatRoomMessageResponse::fromEntity);
    }

    // TODO: 추후 쿼리 최적화가 필요..
    @Transactional
    public CreateChatRoomResponse createChatRoom(CreateChatRoomRequest createChatRoomRequest) {
        // project 정보 찾기
        Project project = projectRepository.findByIdOrThrow(createChatRoomRequest.getProjectId());

        // 이미 채팅방이 있지 않은지 검증 => 프로젝트 아이디와 스타터 아이디가 동일한게 존재하면 Exception
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findByStarterMemberIdAndProject(createChatRoomRequest.getChatStarterMemberId(), project);
        if(findChatRoom.isPresent()) {
            throw new BaseException(CHATROOM_ALREADY_EXIST);
        }

        // chatroom 만들기
        ChatRoom chatRoom = CreateChatRoomRequest.toEntity(project, createChatRoomRequest);

        // chatParticipant 만들기
        Member starterMember = memberRepository.findByIdOrThrow(createChatRoomRequest.getChatStarterMemberId());
        Member pmMember = memberRepository.findByIdOrThrow(project.getPm().getId());

        ChatParticipant starter = ChatParticipant.builder()
                .member(starterMember)
                .chatRoom(chatRoom)
                .build();
        ChatParticipant pm = ChatParticipant.builder()
                .member(pmMember)
                .chatRoom(chatRoom)
                .build();

        chatParticipantRepository.save(starter);
        chatParticipantRepository.save(pm);

        List<ChatParticipant> participants = new ArrayList<>();
        participants.add(starter);
        participants.add(pm);
        chatRoom.setChatParticipants(participants);

        return CreateChatRoomResponse.fromEntity(chatRoom);
    }

    // unread message 읽음 처리
    private void resolveUnread(Long chatRoomId, Long memberId) {
        // 방 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);

        // 상대방 찾기
        Member participant = chatRoom.getChatParticipants().stream()
                .filter(p -> !p.getMember().getId().equals(memberId))
                .findFirst()
                .map(ChatParticipant::getMember)
                .orElseThrow(() -> new BaseException(INVALID_CHATROOM));

        // 안읽은 메시지 모두 가져오기
        List<ChatMessage> unreadMessages = chatMessageRepository.findChatMessageByChatRoomAndIsReadAndMember(chatRoom, false, participant);

        // 메시지 읽기
        unreadMessages.stream().forEach(ChatMessage::readMessage);
    }

}

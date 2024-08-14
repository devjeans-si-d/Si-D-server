package org.devjeans.sid.domain.chatRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.component.ConnectedMap;
import org.devjeans.sid.domain.chatRoom.dto.*;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatParticipantRepository;
import org.devjeans.sid.domain.chatRoom.repository.ChatRoomRepository;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.AuthException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.devjeans.sid.global.exception.exceptionType.AuthException.FORBIDDEN;
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
    private final ConnectedMap connectedMap;
    private final SecurityUtil securityUtil;

    // 해당 회원이 속한 채팅방을 updatedAt DESC로 정렬해서 보여주기
    public Page<ChatRoomSimpleResponse> getChatRoomList(Pageable pageable) {
        Long memberId = securityUtil.getCurrentMemberId();

        // 검증
        if(!memberId.equals(securityUtil.getCurrentMemberId())) { // 현재 로그인한 유저의 아이디와 memberId가 같지 않다면
            throw new BaseException(FORBIDDEN);

        }

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
    public Slice<ChatRoomMessageResponse> getChatRoomMessages(Pageable pageable, Long chatRoomId) {
        Long memberId = securityUtil.getCurrentMemberId();
        resolveUnread(chatRoomId, memberId);
//        Slice<ChatMessage> messages = chatMessageRepository.findAllByChatRoomId(pageable, chatRoomId);
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

    // TODO: FRONT - 만약 방을 만들려고 했는데 createChatRoom에서 CHATROOM_ALREADY_EXIST가 떨어지면 enterChatRoom 호출
    public void enterChatRoom(Long chatRoomId) {
        Long memberId = securityUtil.getCurrentMemberId();

        // 검증: 해당 방에 member가 속해있는지
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);
        List<Long> memberIds = chatRoom.getChatParticipants().stream()
                .map(p -> p.getMember().getId())
                .collect(Collectors.toList());

        if(!memberIds.contains(memberId)) {
            // 회원이 아닌 경우
            throw new BaseException(NOT_A_PARTICIPANT);
        }

        // 메모리에 저장
        connectedMap.enterChatRoom(chatRoomId, memberId);
    }

    public List<MemberInfoResponse> getMemberInfo(Long chatroomId) {
        Long memberId = securityUtil.getCurrentMemberId();

        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatroomId);
        // 상대방 찾기
        Member participant = chatRoom.getChatParticipants().stream()
                .filter(p -> !p.getMember().getId().equals(memberId))
                .findFirst()
                .map(ChatParticipant::getMember)
                .orElseThrow(() -> new BaseException(INVALID_CHATROOM));

        // 나 찾기
        Member me = memberRepository.findByIdOrThrow(memberId);

        MemberInfoResponse participantInfo = MemberInfoResponse.fromEntity(participant);
        MemberInfoResponse meInfo = MemberInfoResponse.fromEntity(me);

        return new ArrayList<>(List.of(participantInfo, meInfo));
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

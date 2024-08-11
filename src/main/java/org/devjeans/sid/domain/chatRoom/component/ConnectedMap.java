package org.devjeans.sid.domain.chatRoom.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ConnectedMap {
    private static final Map<Long, Long> memberIdToChatroomId = new ConcurrentHashMap<>(); // memberId, chatRoomId
    private static final Map<String, Long> sessionToMemberId = new ConcurrentHashMap<>(); // 세션 아이디, ChatRoomValue

    public Long getChatroomIdByMemberId(Long memberId) {
        return memberIdToChatroomId.get(memberId);
    }

    public Long getMemberIdBySessionId(String sessionId) {
        return sessionToMemberId.get(sessionId);
    }

    // 유저가 채팅방에 입장했을 때
    public Long updateChatRoomId(Long memberId, Long chatRoomId) {
        memberIdToChatroomId.put(memberId, chatRoomId);
        return chatRoomId;
    }

    public void exitRoom(String sessionId) {
        // memberIdToChatRoomId, sessionToMemberId에서 지워주기
        Long memberId = sessionToMemberId.get(sessionId);
        log.info("[connected map line 28 exit]: member id: " + memberId);
        memberIdToChatroomId.remove(memberId);
        sessionToMemberId.remove(sessionId);
    }

    public void enterChatRoom(Long chatRoomId, Long memberId) {
        memberIdToChatroomId.put(memberId, chatRoomId);

    }

    public void addSession(String sessionId, Long memberId) {
        sessionToMemberId.put(sessionId, memberId);
        log.info("sessionTo {}", sessionToMemberId);
        log.info("memberIdTo {}", memberIdToChatroomId);
    }
}

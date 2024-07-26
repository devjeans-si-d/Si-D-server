package org.devjeans.sid.domain.chatRoom.component;

import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.controller.ChatRoomValue;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ConnectedMap {
    private static final Map<Long, Long> memberIdToChatroomId = new ConcurrentHashMap<>(); // memberId, chatRoomId
    private static final Map<String, ChatRoomValue> sessionToChatroom = new ConcurrentHashMap<>(); // 세션 아이디, ChatRoomValue

    public Long getChatroomIdByMemberId(Long memberId) {
        return memberIdToChatroomId.get(memberId);
    }

    // 유저가 채팅방에 입장했을 때
    public Long updateChatRoomId(Long memberId, Long chatRoomId) {
        memberIdToChatroomId.put(memberId, chatRoomId);
        return chatRoomId;
    }

    public void exitRoom(Long memberId) {
        // memberIdToChatRoomId에서 지워주기
        log.info("[connected map line 28 exit]: member id: " + memberId);
        memberIdToChatroomId.remove(memberId);
    }

    public void enterChatRoom(Long chatRoomId, Long memberId) {
        log.info("[connected map line 39 enter]: member id, chatrooid: " + memberId + " " + chatRoomId);
        memberIdToChatroomId.put(memberId, chatRoomId);
    }
}
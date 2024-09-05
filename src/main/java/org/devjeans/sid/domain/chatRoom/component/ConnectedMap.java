package org.devjeans.sid.domain.chatRoom.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ConnectedMap {
    @Qualifier("chatPubSub")
    private final RedisTemplate<String, Object> redisTemplate;

    public ConnectedMap(@Qualifier("chatPubSub") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getChatroomIdByMemberId(Long memberId) {
        log.info("redis line 21: {}", redisTemplate.opsForValue().get("member_" + memberId));
        try {
            return (String) redisTemplate.opsForValue().get("member_" + memberId);
        } catch(Exception e) {
            return null;
        }
    }

    public Long getMemberIdBySessionId(String sessionId) {
        try {
            return Long.parseLong((String) redisTemplate.opsForValue().get("session_" + sessionId));
        } catch(Exception e) {
            return null;
        }

    }

    // 유저가 채팅방에 입장했을 때
    public Long updateChatRoomId(Long memberId, Long chatRoomId) {
        redisTemplate.opsForValue().set("member_" + memberId, chatRoomId);
        return chatRoomId;
    }

    public void exitRoom(String sessionId) {
        // memberIdToChatRoomId, sessionToMemberId에서 지워주기
        String memberId = (String) redisTemplate.opsForValue().get("session_" + sessionId);
        redisTemplate.delete("member_" + memberId);
        redisTemplate.delete("session_" + sessionId);
    }

    public void enterChatRoom(Long chatRoomId, Long memberId) {
        log.info("redis line 52!!!!!");
        redisTemplate.opsForValue().set("member_" + memberId, Long.toString(chatRoomId));

    }

    public void addSession(String sessionId, Long memberId) {
        redisTemplate.opsForValue().set("session_" + sessionId, Long.toString(memberId));
    }
}

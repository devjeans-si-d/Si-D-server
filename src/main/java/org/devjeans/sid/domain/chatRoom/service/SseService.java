package org.devjeans.sid.domain.chatRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.dto.sse.NotificationResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseChatResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseEnterResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseTeamBuildResponse;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.devjeans.sid.global.exception.exceptionType.SseExceptionType.FAIL_TO_NOTIFY;

// TODO: SseService는 인증 후 사용 예정
@Slf4j
@RequiredArgsConstructor
@Service
public class SseService {

    // key: memberId
    private static final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();
    private final SecurityUtil securityUtil;

    // 로그인하면 emitter 생성
    public SseEmitter subscribe() {
        Long memberId = securityUtil.getCurrentMemberId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.put(memberId, emitter);
        emitter.onCompletion(() -> clients.remove(memberId));
        emitter.onTimeout(() -> clients.remove(memberId));
        return emitter;
    }


    // 로그아웃시 호출
    public void completeEmitter() {
        Long memberId = securityUtil.getCurrentMemberId();
        SseEmitter emitter = clients.get(memberId);
        if (emitter != null) {
            emitter.complete();
            clients.remove(memberId);
        }
    }

    // 채팅 보낼 때 전송할 것임 (StompHandler에서)
    // type: chat
    public void sendChatNotification(Long memberId, SseChatResponse sseChatResponse) {
        SseEmitter emitter = clients.get(memberId);
        NotificationResponse noti = new NotificationResponse("chat", sseChatResponse, LocalDateTime.now());
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        if (emitter != null) {
            try {
                String notification = om.writeValueAsString(noti);
                emitter.send(notification);
            } catch (IOException e) {
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }

    // 채팅방에 들어올 때 전송 => Chat List를 refresh 시키기 위함
    // type: enter
    public void sendEnterChatroom(Long memberId, SseEnterResponse sseEnterResponse) {
        SseEmitter emitter = clients.get(memberId);
        NotificationResponse noti = new NotificationResponse("enter", sseEnterResponse, LocalDateTime.now());

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        if (emitter != null) {
            try {
                String notification = om.writeValueAsString(noti);
                emitter.send(notification);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }

    // 프로젝트가 마감됐을때 모든 팀원들에게 알림 TODO: 어디서 마감되는지 물어봐야겠다.
    // type: team
    public void sendTeamBuild(Long memberId, SseTeamBuildResponse sseTeamBuildResponse) {
        SseEmitter emitter = clients.get(memberId);
        NotificationResponse noti = new NotificationResponse("team", sseTeamBuildResponse, LocalDateTime.now());

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        if (emitter != null) {
            try {
                String notification = om.writeValueAsString(noti);
                emitter.send(notification);
            } catch (IOException e) {
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }
}


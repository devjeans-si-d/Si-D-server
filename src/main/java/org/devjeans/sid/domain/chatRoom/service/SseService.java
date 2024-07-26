package org.devjeans.sid.domain.chatRoom.service;

import org.devjeans.sid.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.devjeans.sid.global.exception.exceptionType.SseExceptionType.FAIL_TO_NOTIFY;

// TODO: SseService는 인증 후 사용 예정
@Service
public class SseService {

    // key: memberId
    private static final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

    // 로그인하면 emitter 생성
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.put(memberId, emitter);
        emitter.onCompletion(() -> clients.remove(memberId));
        emitter.onTimeout(() -> clients.remove(memberId));
        return emitter;
    }


    // 로그아웃시 호출
    public void completeEmitter(Long memberId) {
        SseEmitter emitter = clients.get(memberId);
        if (emitter != null) {
            emitter.complete();
            clients.remove(memberId);
        }
    }

    // 채팅 보낼 때 전송할 것임 (StompHandler에서)
    public void sendNotification(String memberId, String notification) {
        SseEmitter emitter = clients.get(memberId);

        if (emitter != null) {
            try {
                emitter.send(notification);
            } catch (IOException e) {
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }
}


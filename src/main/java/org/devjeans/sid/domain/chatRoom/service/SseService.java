package org.devjeans.sid.domain.chatRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.dto.sse.NotificationResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseChatResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseEnterResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseTeamBuildResponse;
import org.devjeans.sid.domain.chatRoom.entity.Alert;
import org.devjeans.sid.domain.chatRoom.entity.AlertType;
import org.devjeans.sid.domain.chatRoom.repository.AlertRepository;
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
    private final AlertRepository alertRepository;

    // 로그인하면 emitter 생성
    public SseEmitter subscribe() {
        Long memberId = securityUtil.getCurrentMemberId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.put(memberId, emitter);
        emitter.onCompletion(() -> clients.remove(memberId));
        emitter.onTimeout(() -> clients.remove(memberId));

        try {
            // "connect": 이벤트 이름
            // "connected!!" : 메시지 내용
            emitter.send(SseEmitter.event().name("connect").data("connected!!"));
        } catch(IOException e) {
            e.printStackTrace();
        }

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
        
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("chat").data(noti));
            } catch (IOException e) {
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }


    // 프로젝트가 마감됐을때 모든 팀원들에게 알림 TODO: 어디서 마감되는지 물어봐야겠다.
    // type: team
    public void sendTeamBuild(Long memberId, SseTeamBuildResponse sseTeamBuildResponse) {
        SseEmitter emitter = clients.get(memberId);
        NotificationResponse noti = new NotificationResponse("team", sseTeamBuildResponse, LocalDateTime.now());

        // 알림을 DB에 저장하기
        String content;
        if(sseTeamBuildResponse.getPmId().equals(memberId)) {
            content = "내가 PM(Leader)인 " + sseTeamBuildResponse.getProjectName() + " 프로젝트의 모집이 종료되었어요. " +
                    "수신 메일함을 확인하여 팀원들에게 연락을 취해보세요!";
        } else {
            content = "내가 속해 있는 " + sseTeamBuildResponse.getProjectName() + " 프로젝트의 모집이 종료되었어요." +
                    " 멋진 동료들과 좋은 서비스를 만드는 경험이 되기를 바랄게요!";
        }

        Alert alert = Alert.builder()
                .title("프로젝트 모집 종료!")
                .content(content)
                .alertType(AlertType.TEAM)
                .isRead("N")
                .memberId(memberId)
                .build();

        alertRepository.save(alert);


        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("team").data(noti));
            } catch (IOException e) {
                throw new BaseException(FAIL_TO_NOTIFY);
            }
        }
    }
}


package org.devjeans.sid.domain.chatRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.component.ConnectedMap;
import org.devjeans.sid.domain.chatRoom.dto.RedisRes;
import org.devjeans.sid.domain.chatRoom.dto.sse.NotificationResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseChatResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseEnterResponse;
import org.devjeans.sid.domain.chatRoom.dto.sse.SseTeamBuildResponse;
import org.devjeans.sid.domain.chatRoom.entity.Alert;
import org.devjeans.sid.domain.chatRoom.entity.AlertType;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.repository.AlertRepository;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.devjeans.sid.global.exception.exceptionType.SseExceptionType.FAIL_TO_NOTIFY;

// TODO: SseService는 인증 후 사용 예정
@Slf4j
@Service
public class SseService implements MessageListener {

    // key: memberId
    private static final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();
    private Set<Long> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("ssePubSub")
    private final RedisTemplate<String, Object> sseRedisTemplate;
    private final SecurityUtil securityUtil;
    private final AlertRepository alertRepository;

//    @Qualifier("ssePubSub")
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final ConnectedMap connectedMap;


    public SseService(@Qualifier("ssePubSub")
                      RedisTemplate<String, Object> sseRedisTemplate,
                      SecurityUtil securityUtil,
                      AlertRepository alertRepository,
                      RedisMessageListenerContainer redisMessageListenerContainer,
                      ConnectedMap connectedMap) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.securityUtil = securityUtil;
        this.alertRepository = alertRepository;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.connectedMap = connectedMap;
    }

    // 로그인하면 emitter 생성
    public SseEmitter subscribe() {
        Long memberId = securityUtil.getCurrentMemberId();

        log.info("memberId: {}", memberId);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.put(memberId, emitter);

        log.info("clients: {}", clients);
        emitter.onCompletion(() -> clients.remove(memberId));
        emitter.onTimeout(() -> clients.remove(memberId));

        try {
            // "connect": 이벤트 이름
            // "connected!!" : 메시지 내용
            log.info("line 55 !! connect");
            emitter.send(SseEmitter.event().name("connect").data("connected!!"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        subscribeChannel(memberId);
        return emitter;
    }

    public void subscribeChannel(Long memberId) {
//        이미 구독한 email일 경우에는 더이상 구독하지 않는 분기처리
        if (!subscribeList.contains(memberId)) {
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(String.valueOf(memberId)));
            subscribeList.add(memberId);
        }
    }

    private MessageListenerAdapter createListenerAdapter(SseService sseService) {
        return new MessageListenerAdapter(sseService, "onMessage");
    }

    public void publishMessage(RedisRes redisRes, Long memberId) {
        SseEmitter emitter = clients.get(memberId);
//        if(emitter != null){
//            try {
//                emitter.send(SseEmitter.event().name("chat").data(redisRes));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }else{
        sseRedisTemplate.convertAndSend(String.valueOf(memberId), redisRes);
//        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("온메시지 여기");
        try {
            Long memberId = Long.valueOf(new String(pattern, StandardCharsets.UTF_8));
            SseEmitter emitter = clients.get(memberId);

            RedisRes redisRes = objectMapper.readValue(message.getBody(), RedisRes.class);
            if(redisRes.getType().equals("chat")){
                NotificationResponse noti = new NotificationResponse("chat", redisRes.getData(), LocalDateTime.now());
                LinkedHashMap lm = (LinkedHashMap) redisRes.getData();

                // 이미 접속 중이기 때문에 빠져나온다.
                String chatroom = connectedMap.getChatroomIdByMemberId(memberId);
                if(chatroom != null && lm.get("chatroomId").equals(chatroom)) {
                    return;
                }

//                if (emitter != null) {
                emitter.send(SseEmitter.event().name("chat").data(noti));
                System.out.println("채팅 여기");
//                }
            } else {
                NotificationResponse noti = new NotificationResponse("team", redisRes.getData(), LocalDateTime.now());
//                if (emitter != null) {
                emitter.send(SseEmitter.event().name("team").data(noti));
                System.out.println("팀 빌딩 여기");
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //=================================================
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

        if (emitter != null) {
            publishMessage(new RedisRes("chat",sseChatResponse), memberId);
        }
    }


    // 프로젝트가 마감됐을때 모든 팀원들에게 알림 TODO: 어디서 마감되는지 물어봐야겠다.
    // type: team
    public void sendTeamBuild(Long memberId, SseTeamBuildResponse sseTeamBuildResponse, List<ProjectMember> projectMemberList) {
        SseEmitter emitter = clients.get(memberId);
        NotificationResponse noti = new NotificationResponse("team", sseTeamBuildResponse, LocalDateTime.now());

        // 알림을 DB에 저장하기
        StringBuilder content;
        if(sseTeamBuildResponse.getPmId().equals(memberId)) {
            content = new StringBuilder("내가 PM(Leader)인 " + sseTeamBuildResponse.getProjectName() + " 프로젝트의 모집이 종료되었어요. \r\n");
            content.append("\r\n팀원 정보: \r\n");

            for (ProjectMember member : projectMemberList) {
                if(member.getMember().getId().equals(memberId)) {
                    continue;
                }

                String msg = "\r\n 이름: " + member.getMember().getName() + " / 전화번호: " + member.getMember().getPhoneNumber() + "\r\n";
                content.append(msg);
            }
            content.append("\r\n멋진 동료들과 좋은 서비스를 만드는 경험이 되기를 바랄게요!");
        } else {
            content = new StringBuilder("내가 속해 있는 " + sseTeamBuildResponse.getProjectName() + " 프로젝트의 모집이 종료되었어요.\r\n" +
                    "멋진 동료들과 좋은 서비스를 만드는 경험이 되기를 바랄게요!");
        }

        Alert alert = Alert.builder()
                .title("프로젝트 모집 종료!")
                .content(content.toString())
                .alertType(AlertType.TEAM)
                .isRead("N")
                .memberId(memberId)
                .build();

        alertRepository.save(alert);


//        if (emitter != null) {
        sseRedisTemplate.convertAndSend(String.valueOf(memberId), new RedisRes("team",sseTeamBuildResponse));
//            try {
//                emitter.send(SseEmitter.event().name("team").data(noti));
//            } catch (IOException e) {
//                throw new BaseException(FAIL_TO_NOTIFY);
//            }
//        }
    }
}


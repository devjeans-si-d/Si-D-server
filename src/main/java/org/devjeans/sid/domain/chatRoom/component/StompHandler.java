package org.devjeans.sid.domain.chatRoom.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.auth.JwtTokenProvider;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.TokenException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.devjeans.sid.global.exception.exceptionType.TokenException.INVALID_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 구성된 인터셉터들 간의 작업 우선순위를 지정해서 최우선순위를 부여
public class StompHandler implements ChannelInterceptor {
    private final ConnectedMap connectedMap;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 메시지가 실제로 채널로 전송되기 전에 호출된다.
     * publisher가 send하기 전에 실행
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // StompHeaderAccessor는 STOMP 프로토콜 상의 메시지 헤더를 편리하게 다루기 위한 도구.
        // StompHeaderAccessor.wrap()으로 메시지를 감싸면 STOMP의 헤더에 직접 접근할 수 있다.
        // 클라이언트에서 첫 연결 시 헤더에 TOKEN을 담아주면 인증 절차가 진행된다.
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("preSend 진입");
        // 웹소켓 연결 시 헤더의 jwt token 유효성 검증
        String bearerToken = null;
        if (StompCommand.CONNECT == accessor.getCommand()) {
            // "Authorization"이라는 이름의 헤더를 찾아 그 헤더의 첫 번째 값을 반환.
            // Authorization 헤더의 형식 => `Authorization: <type> <credentials>`
            // type: 사용하는 인증 방식.(e.g., Bearer)
            // credentials: 인증 방식에 따른 인증 정보(토큰)를 의미한다. 발급받은 JWT 토큰
            bearerToken = accessor.getFirstNativeHeader("Authorization");
            // 토큰 검증
            jwtTokenProvider.validateWebSocketToken(bearerToken);

            // 저장
            String sessionId = accessor.getSessionId();
            Long memberId = jwtTokenProvider.getMemberIdFromToken(bearerToken);
            connectedMap.addSession(sessionId, memberId);
        }

        if (StompCommand.DISCONNECT == accessor.getCommand()) {
            log.info("WebSocket DISCONNECT");
            String sessionId = accessor.getSessionId();
            connectedMap.exitRoom(sessionId);
        }

        return message;
    }
}
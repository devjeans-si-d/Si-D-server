package org.devjeans.sid.domain.chatRoom.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 구성된 인터셉터들 간의 작업 우선순위를 지정해서 최우선순위를 부여
public class StompHandler implements ChannelInterceptor {
    private final ConnectedMap connectedMap;

//    private final JwtProvider jwtProvider;

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

        log.info("preSend 진입했나?");
        log.info("[line 34] " + accessor);
        log.info("[line 34] " + message.getPayload());
        // 웹소켓 연결 시 헤더의 jwt token 유효성 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("웹소켓 토큰 검증 시작!");
            // "Authorization"이라는 이름의 헤더를 찾아 그 헤더의 첫 번째 값을 반환.
            // "Authorization" 헤더는 클라이언트가 서버로 보낸 요청에 대한 인증 정보(JWT 토큰)를 포함한다.
            // Authorization 헤더의 형식 => `Authorization: <type> <credentials>`
            // type: 사용하는 인증 방식.(e.g., Bearer)
            // credentials: 인증 방식에 따른 인증 정보(토큰)를 의미한다. 발급받은 JWT 토큰!
//            final String authorization = accessor.getFirstNativeHeader("Authorization");

            // 토큰 검증
//            jwtProvider.validateWebSocketToken(authorization);


            // 토큰 검증 통과
//            log.info("토큰 검증 통과! WebSocket CONNECT!");
        }

        if (StompCommand.DISCONNECT == accessor.getCommand()) {
            log.info("WebSocket DISCONNECT!");
            // TODO: 여기서 토큰을 깐다음에, 멤버아이디 꺼내와서 세션에서 삭제해주면 되겠다.
            connectedMap.exitRoom(1L); // FIXME: 현재는 1L로 고정이지만, 추후 memberId로 변경
        }

        return message;
    }
}
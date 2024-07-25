package org.devjeans.sid.domain.chatRoom.component;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.chatRoom.service.ChatService;
import org.devjeans.sid.domain.chatRoom.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
    private final ConnectedMap connectedMap;
    private final WebSocketService webSocketService;
    private final ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

//        chatService.enterChatRoom(sessionId);

        logger.info("Web socket connected, sessionId: " + sessionId + "  " + event.toString());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        logger.info("[line 33] Web socket disconnected, sessionId: " + sessionId);
        // 여기서 추가적인 처리를 할 수 있습니다.

    }
}

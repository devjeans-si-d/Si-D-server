package org.devjeans.sid.domain.chatRoom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.dto.ChatMessageRequest;
import org.devjeans.sid.domain.chatRoom.repository.ChatMessageRepository;
import org.devjeans.sid.domain.chatRoom.service.WebSocketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebSocketController {

    private final WebSocketService webSocketService;

    // 웹소켓 메시지를 특정 경로로 매핑한다.
    @MessageMapping("/{chatRoomId}/receiver/{receiverId}") // /pub/1/receiver/1
//    @SendTo("/sub/chatroom/{chatRoomId}") // /sub/chatroom/1
    public void sendMessage(ChatMessageRequest chatMessageRequest,
                            SimpMessageHeaderAccessor headerAccessor,
                            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
                            @DestinationVariable(value = "receiverId") Long receiverId) {
        log.info("line 35: {}", chatMessageRequest);
        webSocketService.sendMessage(chatRoomId, receiverId, chatMessageRequest);
    }


}
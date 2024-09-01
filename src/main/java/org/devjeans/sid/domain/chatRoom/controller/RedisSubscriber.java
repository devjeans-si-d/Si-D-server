package org.devjeans.sid.domain.chatRoom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.dto.ChatMessageRequest;
import org.devjeans.sid.domain.chatRoom.dto.ChatRoomMessageResponse;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ChatExceptionType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.*;

@Slf4j
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    public RedisSubscriber(ObjectMapper objectMapper,
                           @Qualifier("chatPubSub") RedisTemplate<String, Object> redisTemplate,
                           SimpMessageSendingOperations messagingTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    //== Redis Chat Subscribe ==//
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());

        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatRoomMessageResponse roomMessage = objectMapper.readValue(publishMessage, ChatRoomMessageResponse.class);

            messagingTemplate.convertAndSend("/sub/chatroom/" + roomMessage.getChatroomId(), roomMessage);


        } catch (Exception e) {
            throw new BaseException(INVALID_CHATROOM);
        }
    }
}

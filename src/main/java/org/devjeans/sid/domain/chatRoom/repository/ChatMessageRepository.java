package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findAllByIsReadAndChatRoomIdOrderByUpdatedAt(Pageable pageable, Boolean isRead, Long chatRoomId);
//    Long countChatMessageByIsRead(Boolean isRead);
}

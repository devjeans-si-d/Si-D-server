package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
}

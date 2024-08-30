package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatParticipant;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findAllByMemberId(Long memberId);

    List<ChatParticipant> findAllByChatRoom(ChatRoom chatRoom);
}

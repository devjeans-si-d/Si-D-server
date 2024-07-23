package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select r from ChatRoom r inner join ChatParticipant p on r.id = p.chatRoom.id where p.id = :memberId order by r.updatedAt desc")
    Page<ChatRoom> findAllByMemberIdOrderByUpdatedAtDesc(Pageable pageable, @Param("memberId") Long memberId);
}

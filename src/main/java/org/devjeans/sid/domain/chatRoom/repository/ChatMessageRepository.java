package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.dto.ChatRoomMessageResponse;
import org.devjeans.sid.domain.chatRoom.entity.ChatMessage;
import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    Page<ChatMessage> findAllByAndChatRoomIdOrderByUpdatedAt(Pageable pageable, Long chatRoomId);
    Long countChatMessageByChatRoomAndIsReadAndMember(ChatRoom chatRoom, Boolean isRead, Member member);

    @Query("select m from ChatMessage m where m.chatRoom = :chatRoom order by m.createdAt desc")
    List<ChatMessage> findRecentMessageByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

//    @Query("select m from ChatMessage m where m.chatRoom.id = :chatRoomId order by m.createdAt desc")
//    Slice<ChatMessage> findAllByChatRoomId(Pageable pageable, @Param("chatRoomId") Long chatRoomId);

    @Query("select m from ChatMessage m where m.chatRoom.id = :chatRoomId")
    List<ChatMessage> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    List<ChatMessage> findChatMessageByChatRoomAndIsReadAndMember(ChatRoom chatRoom, boolean b, Member participant);
}

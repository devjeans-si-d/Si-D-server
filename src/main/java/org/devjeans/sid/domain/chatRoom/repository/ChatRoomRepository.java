package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.ChatRoom;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ChatExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.ChatExceptionType.NO_CHATROOM;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select r from ChatRoom r inner join fetch ChatParticipant p on r.id = p.chatRoom.id where p.chatRoom.id = :memberId order by r.updatedAt desc")
    Page<ChatRoom> findAllByMemberIdOrderByUpdatedAtDesc(Pageable pageable, @Param("memberId") Long memberId);

    @Query("select r from ChatRoom r where r.id in :ids and r.chatMessages.size >= 1 order by r.updatedAt")
    Page<ChatRoom> findAllByIds(Pageable pageable, @Param("ids") List<Long> ids);

    Optional<ChatRoom> findByStarterMemberIdAndProject(Long chatStarterMemberId, Project project);

    default ChatRoom findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(NO_CHATROOM));
    }


}

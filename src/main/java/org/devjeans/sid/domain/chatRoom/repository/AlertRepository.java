package org.devjeans.sid.domain.chatRoom.repository;

import org.devjeans.sid.domain.chatRoom.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByMemberIdAndIsRead(Long memberId, String isRead);

    List<Alert> findAllByMemberId(Long memberId);

    List<Alert> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}

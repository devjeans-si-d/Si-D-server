package org.devjeans.sid.domain.siderCard.repository;

import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.SiderCardExceptionType.SIDER_CARD_MEMBER_NOT_FOUND;


public interface SiderCardRepository extends JpaRepository<SiderCard,Long> {
    default SiderCard findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(SIDER_CARD_MEMBER_NOT_FOUND));
    }
}

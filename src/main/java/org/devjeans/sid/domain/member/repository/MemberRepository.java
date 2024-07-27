package org.devjeans.sid.domain.member.repository;

import org.devjeans.sid.global.exception.BaseException;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.MEMBER_NOT_FOUND;

import org.devjeans.sid.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
    }

    Optional<Member> findBySocialId(Long socialId);
}


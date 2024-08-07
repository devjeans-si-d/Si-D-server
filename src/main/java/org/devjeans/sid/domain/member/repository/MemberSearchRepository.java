package org.devjeans.sid.domain.member.repository;

import org.devjeans.sid.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSearchRepository extends JpaRepository<Member, Long> {
    Page<Member> findAll(Specification<Member> specification, Pageable pageable);
}

package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    void delete(ProjectMember member);

    //
    @Query("select pm from ProjectMember pm join fetch pm.member join fetch pm.project p where pm.member.id = :memberId order by pm.project.createdAt desc")
    List<ProjectMember> findAllMyProjects(@Param("memberId") Long memberId);

//    @Query("select pm from ProjectMember pm join fetch pm.member, pm.project where pm.member.id = :memberId order by pm.project.createdAt desc")
    Page<ProjectMember> findAllByMemberIdOrderByCreatedAtDesc(Pageable pageable, @Param("memberId") Long memberId);
}

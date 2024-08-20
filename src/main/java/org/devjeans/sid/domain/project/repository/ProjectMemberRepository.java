package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.JobField;
import org.devjeans.sid.domain.member.entity.Member;
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

    Long countByMemberId(@Param("memberId") Long memberId);

    Long countByMemberIdAndJobField(@Param("memberId") Long memberId, @Param("jobField") JobField jobField);

    @Query("select count(pm) from ProjectMember pm where pm.member.id = :memberId AND pm.jobField != 'PM'")
    Long countByTeamMemberId(@Param("memberId") Long memberId);

    @Query("select pm from ProjectMember pm join fetch pm.member join fetch pm.project p where pm.member.id = :memberId order by pm.createdAt desc")
    List<ProjectMember> findAllMyProjects(Pageable pageable, @Param("memberId") Long memberId);

    List<ProjectMember> findAllByMemberId(@Param("memberId") Long memberId);

//    @Query("select pm from ProjectMember pm join fetch pm.member, pm.project where pm.member.id = :memberId order by pm.project.createdAt desc")
    Page<ProjectMember> findAllByMemberIdOrderByCreatedAtDesc(Pageable pageable, @Param("memberId") Long memberId);

    @Query("select pm from ProjectMember pm join fetch pm.member join fetch pm.project p where pm.member.id = :memberId and pm.jobField = :jobField order by pm.createdAt desc")
    List<ProjectMember> findAllMyProjectsByJobField(Pageable pageable, @Param("memberId") Long memberId, @Param("jobField") JobField jobField);

    @Query("select pm from ProjectMember pm join fetch pm.member join fetch pm.project p where pm.member.id = :memberId and p.pm.id != :memberId order by pm.createdAt desc")
    List<ProjectMember> findAllMyTeamProjects(Pageable pageable, @Param("memberId") Long memberId);


//    @Query("SELECT pm.member " +
//            "FROM ProjectMember pm " +
//            "GROUP BY pm.member " +
//            "ORDER BY COUNT(pm.member.id) DESC")
//    Page<Member> findMembersOrderByProjectCountDesc(Pageable pageable);

    @Query("SELECT m " +
            "FROM Member m " +
            "LEFT JOIN ProjectMember pm ON m.id = pm.member.id " +
            "GROUP BY m " +
            "ORDER BY COUNT(pm.id) DESC")
    Page<Member> findMembersOrderByProjectCountDesc(Pageable pageable);
}

package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    Optional<ProjectApplication> findProjectApplicationByMemberIdAndProjectId(Long memberId, Long projectId);

    Page<ProjectApplication> findAllByProjectIdOrderByCreatedAtDesc(Pageable pageable, Long projectId);

    List<ProjectApplication> findAllByProjectIdAndMemberId(Long projectId, Long memberId);

//    @Query("select pa from ProjectApplication pa join fetch Member m join fetch Project p where pa.member.id = :memberId")
    Page<ProjectApplication> findAllByMemberIdOrderByCreatedAtDesc(Pageable pageable, @Param("memberId") Long memberId);

    @Query("SELECT pa.jobField, COUNT(pa) FROM ProjectApplication pa WHERE pa.project.id = :projectId GROUP BY pa.jobField")
    List<Object[]> countApplicationsByJobFieldAndProjectId(@Param("projectId") Long projectId);
}

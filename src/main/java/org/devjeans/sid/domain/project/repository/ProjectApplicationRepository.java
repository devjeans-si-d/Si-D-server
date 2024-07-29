package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    Optional<ProjectApplication> findProjectApplicationByMemberIdAndProjectId(Long memberId, Long projectId);

    Page<ProjectApplication> findAllByProjectIdOrderByCreatedAtDesc(Pageable pageable, Long projectId);
}

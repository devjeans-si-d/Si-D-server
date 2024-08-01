package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    void delete(ProjectMember member);
}

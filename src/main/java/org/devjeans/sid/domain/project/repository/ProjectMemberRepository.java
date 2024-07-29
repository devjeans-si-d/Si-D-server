package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.ProjectAcceptException.NO_APPLICATION_RECORD;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}

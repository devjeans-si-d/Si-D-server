package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.PROJECT_NOT_FOUND;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    default Project findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
    }
}

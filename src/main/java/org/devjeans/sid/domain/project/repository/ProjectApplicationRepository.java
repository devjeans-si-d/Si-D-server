package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectApplication;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ProjectAcceptException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.ProjectAcceptException.*;

public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {

    default ProjectApplication findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(NO_APPLICATION_RECORD));
    }
}

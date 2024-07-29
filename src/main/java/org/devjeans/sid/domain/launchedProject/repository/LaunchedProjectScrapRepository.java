package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.PROJECT_NOT_FOUND;

public interface LaunchedProjectScrapRepository extends JpaRepository<LaunchedProjectScrap, Long> {

    default LaunchedProjectScrap findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
    }

}

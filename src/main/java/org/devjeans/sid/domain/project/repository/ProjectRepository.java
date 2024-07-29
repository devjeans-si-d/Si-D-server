package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.PROJECT_NOT_FOUND;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    // deleteAt이 null인 객체 찾음(삭제되지 않은 객체)
    Optional<Project> findByIdAndDeletedAtIsNull(Long id);

    default Project findByIdOrThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND)); // 원래는 Project Exception타입 던져줘야하는데 임시
    }
}

package org.devjeans.sid.domain.launchedProject.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.LAUNCHED_PROJECT_NOT_FOUND;



@Repository
public interface LaunchedProjectRepository extends JpaRepository<LaunchedProject, Long> {

//    default LaunchedProject findByIdOrThrow(Long id) {
//        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
//    }
        // deleteAt이 null인 객체 찾음(삭제되지 않은 객체)
        Optional<LaunchedProject> findByIdAndDeletedAtIsNull(Long id);

        default LaunchedProject findByIdOrThrow(Long id) {
                return findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() -> new BaseException(LAUNCHED_PROJECT_NOT_FOUND));
        }


}

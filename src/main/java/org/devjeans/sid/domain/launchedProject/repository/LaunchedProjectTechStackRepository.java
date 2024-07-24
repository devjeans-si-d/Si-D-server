package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.PROJECT_NOT_FOUND;

@Repository
public interface LaunchedProjectTechStackRepository extends JpaRepository<LaunchedProjectTechStack, Long> {
    // LaunchedProjectTechStack 엔티티의 launchedProject 속성의 id를 기준으로 조회 ->
//    List<LaunchedProjectTechStack> findByLaunchedProjectId(Long launchedProjectId);

    default LaunchedProjectTechStack findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
    }
}

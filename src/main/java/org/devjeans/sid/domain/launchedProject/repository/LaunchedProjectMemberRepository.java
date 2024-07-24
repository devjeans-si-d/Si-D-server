package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.PROJECT_NOT_FOUND;

@Repository
public interface LaunchedProjectMemberRepository extends JpaRepository<LaunchedProjectMember, Long> {
//    // LaunchedProject의 id를 기준으로 LaunchedProjectMember 리스트 조회
//    List<LaunchedProjectMember> findByLaunchedProjectId(Long launchedProjectId);

    default LaunchedProjectMember findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
    }
}

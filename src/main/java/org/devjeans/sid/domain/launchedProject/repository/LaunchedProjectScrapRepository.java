package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectScrapExceptionType.SCRAP_LAUNCHED_PROJECT_NOT_FOUND;


public interface LaunchedProjectScrapRepository extends JpaRepository<LaunchedProjectScrap, Long> {

    default LaunchedProjectScrap findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseException(SCRAP_LAUNCHED_PROJECT_NOT_FOUND));
    }

    Optional<LaunchedProjectScrap> findByMemberAndLaunchedProject(Member member, LaunchedProject launchedProject);

    // LaunchedProject-Scrap 교차테이블에서 LaunchedProject id, Member id에 해당하는 데이터가 있는지 확인
    boolean existsByLaunchedProjectIdAndMemberId(Long launchedProjectId, Long memberId);

}

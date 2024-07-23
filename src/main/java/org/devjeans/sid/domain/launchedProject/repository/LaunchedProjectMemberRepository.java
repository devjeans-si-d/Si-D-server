package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaunchedProjectMemberRepository extends JpaRepository<LaunchedProjectMember, Long> {
    // LaunchedProject의 id를 기준으로 LaunchedProjectMember 리스트 조회
    List<LaunchedProjectMember> findByLaunchedProjectId(Long launchedProjectId);
}

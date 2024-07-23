package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaunchedProjectTechStackRepository extends JpaRepository<LaunchedProjectTechStack, Long> {
    // LaunchedProjectTechStack 엔티티의 launchedProject 속성의 id를 기준으로 조회 ->
    List<LaunchedProjectTechStack> findByLaunchedProjectId(Long launchedProjectId);
}

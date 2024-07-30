package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitInfoRepository extends JpaRepository<RecruitInfo, Long> {
}


package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectScrap;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.devjeans.sid.domain.projectScrap.entity.ProjectScrap;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectScrapRepository extends JpaRepository<ProjectScrap,Long> {
}

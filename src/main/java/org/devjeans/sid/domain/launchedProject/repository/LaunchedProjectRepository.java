package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaunchedProjectRepository extends JpaRepository<LaunchedProject, Long> {

}

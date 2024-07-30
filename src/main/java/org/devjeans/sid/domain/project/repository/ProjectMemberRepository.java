package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long>{
    void delete(ProjectMember member);
}

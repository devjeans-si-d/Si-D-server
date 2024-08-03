package org.devjeans.sid.domain.project.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.PROJECT_NOT_FOUND;


@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    // deleteAt이 null인 객체 찾음(삭제되지 않은 객체)
    Optional<Project> findByIdAndDeletedAtIsNull(Long id);
    Page<Project> findByIsClosed(Pageable pageable, String isClosed);
    default Project findByIdOrThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
    }

    // isclosed=n, deletedat!=null, orderby updatedAt
    Page<Project> findByIsClosedAndDeletedAtIsNullOrderByUpdatedAtDesc(Pageable pageable,String isClosed);

    //deletedat!=null, orderby updatedAt
    Page<Project> findByDeletedAtIsNullOrderByUpdatedAtDesc(Pageable pageable);

    // 삭제되지 않고 마감되지 않은 Project 중 스크랩 수 내림차순 정렬
    @Query("SELECT p FROM Project p " +
            "LEFT JOIN p.projectScraps ps " +
            "WHERE p.deletedAt IS NULL AND p.isClosed = 'N' " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(ps) DESC")
    Page<Project> findTopProjects(Pageable pageable);
}

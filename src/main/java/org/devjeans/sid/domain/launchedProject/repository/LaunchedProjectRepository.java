package org.devjeans.sid.domain.launchedProject.repository;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.LAUNCHED_PROJECT_NOT_FOUND;



@Repository
public interface LaunchedProjectRepository extends JpaRepository<LaunchedProject, Long> {

//    default LaunchedProject findByIdOrThrow(Long id) {
//        return findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
//    }
        // deleteAt이 null인 객체 찾음(삭제되지 않은 객체)
        Optional<LaunchedProject> findByProjectIdAndDeletedAtIsNull(Long projectId);

        Optional<LaunchedProject> findByIdAndDeletedAtIsNull(Long id);

        default LaunchedProject findByIdOrThrow(Long id) {
                return findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() -> new BaseException(LAUNCHED_PROJECT_NOT_FOUND));
        }

        // 삭제되지 않은 Launched-Project 중 scrap 리스트 사이즈 내림차 순
        @Query("SELECT lp FROM LaunchedProject lp WHERE lp.deletedAt IS NULL ORDER BY lp.views DESC")
        Page<LaunchedProject> findTopLaunchedProjects(Pageable pageable);

        // 삭제되지 않은 Launched-Project 불러오기
        Page<LaunchedProject> findByDeletedAtIsNull(Pageable pageable);

        // 삭제되지 않고 생성시각 내림차순 정렬 Launched-Project 모두 불러오기
        List<LaunchedProject> findByDeletedAtIsNullOrderByCreatedAtDesc();

}

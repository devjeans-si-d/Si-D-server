package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectMemberRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectTechStackRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LaunchedProjectService {

    private final LaunchedProjectRepository launchedProjectRepository;
    private final LaunchedProjectTechStackRepository launchedProjectTechStackRepository;
    private final LaunchedProjectMemberRepository launchedProjectMemberRepository;

    private static final String STORAGE_DIR = "/Users/wisdom/Documents/devjeans/images"; // 임시 로컬저장 경로

    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository,
                                  LaunchedProjectTechStackRepository launchedProjectTechStackRepository,
                                  LaunchedProjectMemberRepository launchedProjectMemberRepository
                                  ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.launchedProjectTechStackRepository = launchedProjectTechStackRepository;
        this.launchedProjectMemberRepository = launchedProjectMemberRepository;
    }

    // LaunchedProject 등록 (아직 작성중...)
    @Transactional
    public LaunchedProject registerLaunchedProject(){

        return null;
    }

    // LaunchedProject의 id를 기준으로 LaunchedProject 조회
    public LaunchedProject findById(Long id) {
        return launchedProjectRepository.findById(id)
                .orElseThrow(() -> new BaseException(LaunchedProjectExceptionType.PROJECT_NOT_FOUND));
    }

    // LaunchedProjectTechStack의 launchedProject 속성의 id를 기준으로 TechStack 리스트 조회
    public List<LaunchedProjectTechStackResponse> getTechStacksByProjectId(Long projectId) {
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(LaunchedProjectExceptionType.PROJECT_NOT_FOUND));

        // LaunchedProjectTechStack 엔티티의 launchedProject 속성의 id를 기준으로 조회 -> LaunchedProjectTechStack 리스트 반환
        List<LaunchedProjectTechStack> techStacks = launchedProjectTechStackRepository.findByLaunchedProjectId(projectId);

        // DTO로 변환
        return techStacks.stream()
                .map(LaunchedProjectTechStackResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // LaunchedProject의 id를 기준으로 LaunchedProjectMember 리스트 조회
    public List<LaunchedProjectMemberResponse> getProjectMembers(Long projectId) {
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(LaunchedProjectExceptionType.PROJECT_NOT_FOUND));

        // LaunchedProjectMember 리스트 조회
        List<LaunchedProjectMember> members = launchedProjectMemberRepository.findByLaunchedProjectId(projectId);

        // DTO로 변환
        return members.stream()
                .map(LaunchedProjectMemberResponse::fromEntity)
                .collect(Collectors.toList());
    }





}

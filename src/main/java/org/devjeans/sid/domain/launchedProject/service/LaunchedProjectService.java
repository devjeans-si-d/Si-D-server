package org.devjeans.sid.domain.launchedProject.service;

import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.SaveLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectMember;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectMemberRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectTechStackRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.domain.siderCard.repository.TechStackRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.INVALID_PROJECT_IMAGE;

@Service
@Transactional
public class LaunchedProjectService {

    private final LaunchedProjectRepository launchedProjectRepository;
    private final LaunchedProjectTechStackRepository launchedProjectTechStackRepository;
    private final LaunchedProjectMemberRepository launchedProjectMemberRepository;
    private final ProjectRepository projectRepository; // project가 존재하는지 검증하기 위해서 의존성 주입(????)
    private final TechStackRepository techStackRepository;


    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository,
                                  LaunchedProjectTechStackRepository launchedProjectTechStackRepository,
                                  LaunchedProjectMemberRepository launchedProjectMemberRepository,
                                  ProjectRepository projectRepository,
                                  TechStackRepository techStackRepository
                                  ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.launchedProjectTechStackRepository = launchedProjectTechStackRepository;
        this.launchedProjectMemberRepository = launchedProjectMemberRepository;
        this.projectRepository = projectRepository;
        this.techStackRepository = techStackRepository;
    }

    // 파일이 저장될 디렉토리 경로 (아직 로컬 저장소 경로)
    private static final String STORAGE_DIR = "/Users/wisdom/Documents/devjeans/images"; // 임시 로컬저장 경로
    // LaunchedProject 등록
    @Transactional
    // LaunchedProject 등록
    public LaunchedProject registerLaunchedProject(Long projectId,
                                                   SaveLaunchedProjectRequest dto,
                                                   MultipartFile projectImage) {
        // 프로젝트 엔티티 조회
        Project project = projectRepository.findByIdOrThrow(projectId); // 해당 id의 프로젝트가 있는지 확인

        // 이미지 파일 처리 및 로컬 경로 생성
        String imageUrl = null;
        if (projectImage != null && !projectImage.isEmpty()) {
            imageUrl = handleImageUpload(projectImage, projectId); // 로컬경로반환
        }

        // 기술 스택 엔티티 리스트 조회
        List<TechStack> techStacks = techStackRepository.findAllById(dto.getTechStackIds());

        // LaunchedProject 엔티티 생성
        LaunchedProject launchedProject = SaveLaunchedProjectRequest.toEntity(dto, imageUrl, project, techStacks);

        // 엔티티 저장
        return launchedProjectRepository.save(launchedProject);
    }

    private String handleImageUpload(MultipartFile imageFile, Long projectId) {
        // 고유한 파일명 생성 (프로젝트 ID + 원본 파일명)
        String newFileName = projectId + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(STORAGE_DIR, newFileName);

        try {
            Files.write(path, imageFile.getBytes());
        } catch (IOException e) {
            throw new BaseException(INVALID_PROJECT_IMAGE);
        }

        // 로컬 경로 반환
        return path.toString();

    }


    // LaunchedProject의 id를 기준으로 LaunchedProject의 BasicInfo 조회
    public BasicInfoLaunchedProjectResponse getBasicInfoByProjectId(Long projectId) {
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(projectId);
        BasicInfoLaunchedProjectResponse dto = launchedProject.fromEntity(launchedProject);
        return dto;
    }

    // LaunchedProjectTechStack의 launchedProject 속성의 id를 기준으로 TechStack 리스트 조회
    public List<LaunchedProjectTechStackResponse> getTechStacksByProjectId(Long projectId) {
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(projectId);
        // launchedProject의 techStack 리스트
        List<LaunchedProjectTechStack> techStacks = launchedProject.getLaunchedProjectTechStacks();

        // DTO로 변환
        return techStacks.stream()
                .map(LaunchedProjectTechStackResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // LaunchedProject의 id를 기준으로 LaunchedProjectMember 리스트 조회
    public List<LaunchedProjectMemberResponse> getProjectMembers(Long projectId) {
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(projectId);
        List<LaunchedProjectMember> members =  launchedProject.getLaunchedProjectMembers();

        // DTO로 변환
        return members.stream()
                .map(LaunchedProjectMemberResponse::fromEntity)
                .collect(Collectors.toList());
    }

}

package org.devjeans.sid.domain.launchedProject.service;

import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.SaveLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.domain.siderCard.repository.TechStackRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.INVALID_PROJECT_IMAGE;

@Slf4j
@Service
@Transactional
public class LaunchedProjectService {

    private final LaunchedProjectRepository launchedProjectRepository;
    private final ProjectRepository projectRepository; // project가 존재하는지 검증하기 위해서 의존성 주입
    private final TechStackRepository techStackRepository;
    private final MemberRepository memberRepository;


    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository,
                                  ProjectRepository projectRepository,
                                  TechStackRepository techStackRepository,
                                  MemberRepository memberRepository
                                  ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.projectRepository = projectRepository;
        this.techStackRepository = techStackRepository;
        this.memberRepository = memberRepository;
    }

    // 파일이 저장될 디렉토리 경로 (아직 로컬 저장소 경로)
    private static final String STORAGE_DIR = "/Users/wisdom/Documents/devjeans/images"; // 임시 로컬저장 경로

//    private String handleImageUpload(MultipartFile imageFile, Long projectId) {
//        // 고유한 파일명 생성 (프로젝트 ID + 원본 파일명)
//        String newFileName = projectId + "_" + imageFile.getOriginalFilename();
//        Path path = Paths.get(STORAGE_DIR, newFileName);
//
//        try {
//            Files.write(path, imageFile.getBytes());
//        } catch (IOException e) {
//            throw new BaseException(INVALID_PROJECT_IMAGE);
//        }
//
//        // 로컬 경로 반환
//        return path.toString();
//    }

    // LaunchedProject 등록
    public LaunchedProject register(SaveLaunchedProjectRequest dto,
                                    MultipartFile launchedProjectImage
                                    ){

        // dto에서 받은 projectId(Long)로 project 객체 찾음
        Project project = projectRepository.findByIdOrThrow(dto.getProjectId());

        LaunchedProject savedLaunchedProject = null;
        Path imagePath;

        log.info("line 87: {}", dto);

        try{
            byte[] bytes = launchedProjectImage.getBytes(); // 이미지 - > 바이트
            // 경로지정
            imagePath = Paths.get(STORAGE_DIR,  "_"+ launchedProjectImage.getOriginalFilename());
            // 파일 쓰기
            Files.write(imagePath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE); // 해당경로에 bytes 저장

            //launchedProject.updateLaunchedProjectImage(path.toString());
        } catch (IOException e) {
            throw new BaseException(INVALID_PROJECT_IMAGE);
        }

        List<LaunchedProjectTechStack> launchedProjectTechStacks = new ArrayList<>(); // Launched-Project에 사용된 기술스택 리스트

        for(SaveLaunchedProjectRequest.LaunchedProjectTechStackRequest tech : dto.getLaunchedProjectTechStackRequestList()){
            //tech : {"JobField" : "BACKEND", "techStackName" : "Spring"}

            TechStack techStack = techStackRepository.findByIdOrThrow(tech.getId());

            LaunchedProjectTechStack launchedProjectTechStack
                    = LaunchedProjectTechStack.builder()
                    .launchedProject(savedLaunchedProject)
                    .techStack(techStack)
                    .build();

            launchedProjectTechStacks.add(launchedProjectTechStack);
        }

        List<ProjectMember> members = new ArrayList<>();

        for(LaunchedProjectMemberRequest memberDto : dto.getMembers()){
            Member member = memberRepository.findByIdOrThrow(memberDto.getId());
            ProjectMember projectMember = LaunchedProjectMemberRequest.toEntity(memberDto, member, project);
            members.add(projectMember);
        }

        // LaunchedProject 객체로 조립
        LaunchedProject launchedProject = dto.toEntity(dto, imagePath.toString(), project, launchedProjectTechStacks);
        savedLaunchedProject = launchedProjectRepository.save(launchedProject);

        // 저장된 LaunchedProject -> Project -> ProjectMembers에 ProjectMembers 리스트 갈아끼우기
        savedLaunchedProject.getProject().updateProjectMembers(members);

        return savedLaunchedProject;
    }

    // LaunchedProject의 id를 기준으로 LaunchedProject의 BasicInfo 조회
    public BasicInfoLaunchedProjectResponse getBasicInfoByProjectId(Long projectId) {
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(projectId);
//        BasicInfoLaunchedProjectResponse dto = launchedProject.fromEntity(launchedProject);
        return null;
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

//    // LaunchedProject의 id를 기준으로 LaunchedProjectMember 리스트 조회
//    public List<LaunchedProjectMemberResponse> getProjectMembers(Long projectId) {
//        // LaunchedProject가 존재하는지 확인
//        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(projectId);
//        List<LaunchedProjectMember> members =  launchedProject.getLaunchedProjectMembers();
//
//        // DTO로 변환
//        return members.stream()
//                .map(LaunchedProjectMemberResponse::fromEntity)
//                .collect(Collectors.toList());
//    }

}

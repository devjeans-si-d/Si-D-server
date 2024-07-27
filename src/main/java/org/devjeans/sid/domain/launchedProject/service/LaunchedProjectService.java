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

        Path imagePath;

        try{
            byte[] bytes = launchedProjectImage.getBytes(); // 이미지 - > 바이트
            // 경로지정
            imagePath = Paths.get(STORAGE_DIR,  "_"+ launchedProjectImage.getOriginalFilename());
            // 파일 쓰기
            Files.write(imagePath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE); // 해당경로에 bytes 저장

        } catch (IOException e) {
            throw new BaseException(INVALID_PROJECT_IMAGE);
        }

        // LaunchedProject 객체 먼저 조립 (launchedProjectTechStacks 기술스택 리스트 빼고 먼저조립해줌)
        LaunchedProject launchedProject = dto.toEntity(dto, imagePath.toString(), project, new ArrayList<>());

        for(SaveLaunchedProjectRequest.LaunchedProjectTechStackRequest stackDto : dto.getLaunchedProjectTechStackRequestList()){
            //tech : {"JobField" : "BACKEND", "techStackName" : "Spring"}

            TechStack techStack = techStackRepository.findByIdOrThrow(stackDto.getId());

            LaunchedProjectTechStack launchedProjectTechStack
                    = LaunchedProjectTechStack.builder()
                    .launchedProject(launchedProject)
                    .techStack(techStack)
                    .build();

            // 일단 먼저 조립해준 launchedProject 객체의 기술스택 리스트 가져와서 add 해줌
            launchedProject.getLaunchedProjectTechStacks().add(launchedProjectTechStack);
        }

        for(LaunchedProjectMemberRequest memberDto : dto.getMembers()){
            Member member = memberRepository.findByIdOrThrow(memberDto.getId());
            ProjectMember projectMember = LaunchedProjectMemberRequest.toEntity(memberDto, member, project);

            List<ProjectMember> projectMembers = launchedProject.getProject().getProjectMembers();
            // 존재하지 않는 회원만 add (모집 이후 추가되었을 경우)
            if(!projectMembers.contains(member)){
                projectMembers.add(projectMember);
            }
        }

        LaunchedProject savedLaunchedProject = launchedProjectRepository.save(launchedProject);

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
    

}

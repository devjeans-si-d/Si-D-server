package org.devjeans.sid.domain.launchedProject.service;

import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.ListLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.SaveLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.UpdateLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectScrapRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.domain.siderCard.repository.TechStackRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType;
import org.devjeans.sid.global.exception.exceptionType.LaunchedProjectScrapExceptionType;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.DOUBLE_CREATE;
import static org.devjeans.sid.global.exception.exceptionType.LaunchedProjectExceptionType.INVALID_PROJECT_IMAGE;

@Slf4j
@Service
@Transactional
public class LaunchedProjectService {

    private final LaunchedProjectRepository launchedProjectRepository;
    private final LaunchedProjectScrapRepository launchedProjectScrapRepository; // 스크랩
    private final ProjectRepository projectRepository; // project가 존재하는지 검증하기 위해서 의존성 주입
    private final TechStackRepository techStackRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final ProjectMemberRepository projectMemberRepository;
    private final LaunchedProjectViewService launchedProjectViewService;
    private final LaunchedProjectScrapService launchedProjectScrapService;
    private static final String LP_VIEWS_KEY_PREFIX = "launched_project_views:";
    private static final String LP_SCRAP_KEY_PREFIX = "launched_project_scraps:";

    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository,
                                  LaunchedProjectScrapRepository launchedProjectScrapRepository,
                                  ProjectRepository projectRepository,
                                  TechStackRepository techStackRepository,
                                  MemberRepository memberRepository,
                                  SecurityUtil securityUtil,
                                  ProjectMemberRepository projectMemberRepository,
                                  LaunchedProjectViewService launchedProjectViewService,
                                  LaunchedProjectScrapService launchedProjectScrapService
    ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.launchedProjectScrapRepository = launchedProjectScrapRepository;
        this.projectRepository = projectRepository;
        this.techStackRepository = techStackRepository;
        this.memberRepository = memberRepository;
        this.securityUtil = securityUtil;
        this.projectMemberRepository = projectMemberRepository;
        this.launchedProjectViewService = launchedProjectViewService;
        this.launchedProjectScrapService = launchedProjectScrapService;
    }

    // 파일이 저장될 디렉토리 경로 (아직 로컬 저장소 경로)
    private static final String STORAGE_DIR = "/Users/wisdom/Documents/devjeans/images"; // 임시 로컬저장 경로

    public String saveImage(MultipartFile file) {
        Path imagePath;
        try {
            byte[] bytes = file.getBytes(); // 이미지 -> 바이트
            // 경로 지정
            imagePath = Paths.get(STORAGE_DIR, "_" + file.getOriginalFilename());
            // 파일 쓰기
            Files.write(imagePath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE); // 해당 경로에 bytes 저장
        } catch (IOException e) {
            throw new BaseException(INVALID_PROJECT_IMAGE);
        }

        return imagePath.toString(); // 이미지 경로 반환
    }

    // CREATE
    // Launched-Project 등록
    // => 한 Project에 대해서 create 1번만 할 수 있고, PM만 등록할 수 있도록 리팩토링 해야됨.
    @Transactional
    public LaunchedProject register(SaveLaunchedProjectRequest dto){
        // 검증 1. 이미 글이 있지 않은지 확인. 완성글은 프로젝트마다 하나씩 쓸 수 있다.
        Optional<LaunchedProject> projectOpt = launchedProjectRepository.findByProjectIdAndDeletedAtIsNull(dto.getProjectId());
        if(projectOpt.isPresent()) {
            throw new BaseException(DOUBLE_CREATE);
        }

        // dto에서 받은 projectId(Long)로 project 객체 찾음
        Project project = projectRepository.findByIdOrThrow(dto.getProjectId());

        // 우선 기존 프로젝트 멤버를 모두 delete 해준다.
        List<ProjectMember> projectMembers = project.getProjectMembers();
        projectMemberRepository.deleteAll(projectMembers); // deleteAll -> 벌크성 쿼리를 줄여줌


        Path imagePath = null;
        //== TODO: 사진 저장 로직 => S3 presigned url 방식으로 변경한 후 수정 예정 ==//


        // LaunchedProject 객체 먼저 조립 (launchedProjectTechStacks 기술스택 리스트 빼고 먼저조립해줌)
        LaunchedProject launchedProject = dto.toEntity(dto, project, new ArrayList<>());

        for(Long stackId : dto.getTechStackList()){
            //tech : {"JobField" : "BACKEND", "techStackName" : "Spring"}

            TechStack techStack = techStackRepository.findByIdOrThrow(stackId);

            LaunchedProjectTechStack launchedProjectTechStack
                    = LaunchedProjectTechStack.builder()
                    .launchedProject(launchedProject)
                    .techStack(techStack)
                    .build();

            // 일단 먼저 조립해준 launchedProject 객체의 기술스택 리스트 가져와서 add 해줌
            launchedProject.getLaunchedProjectTechStacks().add(launchedProjectTechStack);
        }


        // memberDto를 ProjectMember로 변환
        List<LaunchedProjectMemberRequest> memberDtos = dto.getMembers();
        List<ProjectMember> newMembers = memberDtos.stream().map(memberDto -> {
            Member member = memberRepository.findByIdOrThrow(memberDto.getId());

            return LaunchedProjectMemberRequest.toEntity(memberDto, member, project);
        }).collect(Collectors.toList());

        launchedProject.getProject().updateNewProjectMembers(newMembers); // project에 갈아 끼워주기
        return launchedProjectRepository.save(launchedProject);
    }

    public String update(Long launchedProjectId,
                         UpdateLaunchedProjectRequest dto) {
        // LaunchedProject 객체 찾아서 수정
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(launchedProjectId);

        // project 객체 찾음
        Project project = projectRepository.findByIdOrThrow(launchedProject.getProject().getId());

        // 이미지 수정
        if (dto.getImageUrl() != null) {
            launchedProject.updateLaunchedProjectImage(dto.getImageUrl());
        }

        // 글내용 수정
        if (dto.getLaunchedProjectContents() != null) {
            launchedProject.updateLaunchedProjectContents(dto.getLaunchedProjectContents());
        }

        // 사이트 url 수정
        if (dto.getSiteUrl() != null) {
            launchedProject.updateSiteUrl(dto.getSiteUrl());
        }

        // 새로운 기술 스택 리스트 업데이트
        List<LaunchedProjectTechStack> newTechStacks = new ArrayList<>();
        if (dto.getTechStackList() != null) {
            for (Long stackId : dto.getTechStackList()) {
                TechStack techStack = techStackRepository.findByIdOrThrow(stackId);
                LaunchedProjectTechStack launchedProjectTechStack = UpdateLaunchedProjectRequest.toLaunchedProjectTechStack(launchedProject, techStack);
                newTechStacks.add(launchedProjectTechStack);
            }
            launchedProject.updateLaunchedProjectTechStacks(newTechStacks);
        }

        // 새로운 프로젝트 멤버 리스트 업데이트
        // 우선 기존 프로젝트 멤버를 모두 delete 해준다.
        List<ProjectMember> projectMembers = project.getProjectMembers();
        projectMemberRepository.deleteAll(projectMembers); // deleteAll -> 벌크성 쿼리를 줄여줌
        
        // memberDto를 ProjectMember로 변환
        List<LaunchedProjectMemberRequest> memberDtos = dto.getMembers();
        List<ProjectMember> newMembers = memberDtos.stream().map(memberDto -> {
            Member member = memberRepository.findByIdOrThrow(memberDto.getId());
            return LaunchedProjectMemberRequest.toEntity(memberDto, member, project);
        }).collect(Collectors.toList());
        launchedProject.getProject().updateNewProjectMembers(newMembers); // project에 갈아 끼워주기

        return "수정완료";
    }

    // READ
    // Launched-Project의 id를 기준으로 LaunchedProject의 BasicInfo 조회
    public BasicInfoLaunchedProjectResponse getBasicInfo(Long id) {
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(id);
        BasicInfoLaunchedProjectResponse basicInfoDto = launchedProject.BasicInfoResfromEntity(launchedProject);
        launchedProjectViewService.incrementViews(id); // 조회수 증가
        basicInfoDto.setViews(launchedProjectViewService.getViews(id)); // redis에서 조회수 가져와서 세팅
        basicInfoDto.setScrapCount(launchedProjectScrapService.getScrapCount(id));// redis에서 스크랩수 가져와서 세팅
        return basicInfoDto;
    }

    // Launched-ProjectTechStack의 launchedProject 속성의 id를 기준으로 TechStack 리스트 조회
    public List<LaunchedProjectTechStackResponse> getTechStacks(Long id) {
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(id);

        // launchedProject의 techStack 리스트
        List<LaunchedProjectTechStack> techStacks = launchedProject.getLaunchedProjectTechStacks();

        // LaunchedProject의 techStack responseDTO 리스트
        List<LaunchedProjectTechStackResponse> stackDtoList = techStacks.stream()
                .map(LaunchedProjectTechStack::stackResFromEntity)
                .collect(Collectors.toList());

        return stackDtoList;
    }

    // Launched-Project의 id를 기준으로 참여회원 리스트 조회
    public List<LaunchedProjectMemberResponse> getProjectMembers(Long id){
        // LaunchedProject가 존재하는지 확인
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(id);

        // LaunchedProject -> Project(FK) -> projectMembers 리스트
        List<ProjectMember> projectMembers = launchedProject.getProject().getProjectMembers();

        // LaunchedProject -> Project(FK) -> projectMembersResponseDTO 리스트
        List<LaunchedProjectMemberResponse> memberDtoList = projectMembers.stream()
                .map(ProjectMember::memberResfromEntity)
                .collect(Collectors.toList());

        return memberDtoList;
    }

    // LaunchedProject 리스트 조회
    public Page<ListLaunchedProjectResponse> getList(Pageable pageable){
        Page<LaunchedProject> launchedProjects = launchedProjectRepository.findByDeletedAtIsNull(pageable);

        List<ListLaunchedProjectResponse> listLaunchedProjectResponses = launchedProjects.stream()
                .map(l -> l.listResFromEntity(l,
                        launchedProjectViewService.getViews(l.getId()),
                        launchedProjectScrapService.getScrapCount(l.getId())))
                .collect(Collectors.toList());

        // List -> Page로 변환
        return new PageImpl<>(listLaunchedProjectResponses, pageable ,launchedProjects.getTotalElements());
    }

    // 글을 올린사람은 Launched-Project 글을 삭제할 수 있다.
    public String delete(Long id){
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(id);
        Long pmId = launchedProject.getProject().getPm().getId();
        Long currentMemberId = securityUtil.getCurrentMemberId();

        log.info("line241 memberId :{}",securityUtil.getCurrentMemberId());

        // 만약 지금 로그인한 멤버가 pm이 아닐경우 삭제 권한 없음
        if (!currentMemberId.equals(pmId)) {
            throw new BaseException(LaunchedProjectExceptionType.UNAUTHORIZED_ACTION);
        }

        // 이미 삭제된 게시글인 경우
        if (launchedProject.getDeletedAt() != null) {
            throw new BaseException(LaunchedProjectExceptionType.LAUNCHED_PROJECT_NOT_FOUND);
        }

        launchedProject.setDeletedAt(LocalDateTime.now());
        launchedProjectRepository.save(launchedProject);

        return "성공적으로 삭제되었습니다.";
    }

    // 스크랩 추가
    public LaunchedProjectScrapResponse addScrap(Long launchedProjectId){
        launchedProjectRepository.findByIdOrThrow(launchedProjectId);
        String memberId = securityUtil.getCurrentMemberId().toString();

        // 만약 이미 스크랩한 게시글이라면
        if(launchedProjectScrapService.isScraped(launchedProjectId, memberId)){
            throw new BaseException(LaunchedProjectScrapExceptionType.ALREADY_SCRAPPED);
        }

        launchedProjectScrapService.addScrap(launchedProjectId, memberId);
        Long scrapCount = launchedProjectScrapService.getScrapCount(launchedProjectId);

        return LaunchedProjectScrapResponse.builder()
                .launchedProjectId(launchedProjectId)
                .scrapCount(scrapCount)
                .build();
    }

    // 스크랩 삭제
    public LaunchedProjectScrapResponse removeScrap(Long launchedProjectId){
        launchedProjectRepository.findByIdOrThrow(launchedProjectId);
        String memberId = securityUtil.getCurrentMemberId().toString();

        // 스크랩하지 않은 경우
        if(!launchedProjectScrapService.isScraped(launchedProjectId, memberId)){
            throw new BaseException(LaunchedProjectScrapExceptionType.SCRAP_NOT_FOUND);
        }

        launchedProjectScrapService.removeScrap(launchedProjectId, memberId);
        Long scrapCount = launchedProjectScrapService.getScrapCount(launchedProjectId);

        return LaunchedProjectScrapResponse.builder()
                .launchedProjectId(launchedProjectId)
                .scrapCount(scrapCount)
                .build();
    }

    // 스크랩 상태 확인
    public boolean isScrapped(Long launchedProjectId){
        launchedProjectRepository.findByIdOrThrow(launchedProjectId);
        String memberId = securityUtil.getCurrentMemberId().toString();

        return launchedProjectScrapService.isScraped(launchedProjectId, memberId);
    }
}

package org.devjeans.sid.domain.launchedProject.service;

import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.ListLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.SaveLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.UpdateLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectScrap;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProjectTechStack;
import org.devjeans.sid.domain.launchedProject.entity.ToggleStatus;
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
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    @Autowired
    public LaunchedProjectService(LaunchedProjectRepository launchedProjectRepository,
                                  LaunchedProjectScrapRepository launchedProjectScrapRepository,
                                  ProjectRepository projectRepository,
                                  TechStackRepository techStackRepository,
                                  MemberRepository memberRepository,
                                  SecurityUtil securityUtil,
                                  ProjectMemberRepository projectMemberRepository
                                  ){
        this.launchedProjectRepository = launchedProjectRepository;
        this.launchedProjectScrapRepository = launchedProjectScrapRepository;
        this.projectRepository = projectRepository;
        this.techStackRepository = techStackRepository;
        this.memberRepository = memberRepository;
        this.securityUtil = securityUtil;
        this.projectMemberRepository = projectMemberRepository;
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

    public String update(UpdateLaunchedProjectRequest dto,
                         MultipartFile launchedProjectImage){
        // LaunchedProject 객체 찾아서 수정
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(dto.getId());

        // project 객체 찾음
        Project project = projectRepository.findByIdOrThrow(launchedProject.getProject().getId());

        // 이미지 수정
        if (launchedProjectImage != null) {
            // && !launchedProjectImage.isEmpty()
            String imagePath = saveImage(launchedProjectImage);
            launchedProject.updateLaunchedProjectImage(imagePath);
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
        if (dto.getLaunchedProjectTechStackRequestList() != null) {
            for (UpdateLaunchedProjectRequest.LaunchedProjectTechStackRequest stackDto : dto.getLaunchedProjectTechStackRequestList()) {
                TechStack techStack = techStackRepository.findByIdOrThrow(stackDto.getId());
                LaunchedProjectTechStack launchedProjectTechStack = UpdateLaunchedProjectRequest.toLaunchedProjectTechStack(launchedProject, techStack);
                newTechStacks.add(launchedProjectTechStack);
            }
            launchedProject.updateLaunchedProjectTechStacks(newTechStacks);
        }

        // 새로운 프로젝트 멤버 리스트 업데이트
        if (dto.getMembers() != null) {
            List<ProjectMember> currentProjectMembers = project.getProjectMembers();
            for(ProjectMember member : currentProjectMembers){
                projectMemberRepository.delete(member);
            }

            List<ProjectMember> newProjectMembers = new ArrayList<>();
            for (LaunchedProjectMemberRequest memberDto : dto.getMembers()) {
                Member member = memberRepository.findByIdOrThrow(memberDto.getId());
                ProjectMember projectMember = LaunchedProjectMemberRequest.toEntity(memberDto, member, project);
                newProjectMembers.add(projectMember);

            }
            project.updateNewProjectMembers(newProjectMembers);
            projectRepository.save(project);
        }

        // 변경된 LaunchedProject 저장
        launchedProjectRepository.save(launchedProject);

        return "수정완료";
    }

    // Scrap create/delete : 유저는 한 LaunchedProject 글에 한번만 좋아요를 누를 수 있다 (이미 누른 사용자라면 또 좋아요 눌렀을 때에는 delete처리)
    public LaunchedProjectScrapResponse toggleScrap(LaunchedProjectScrapRequest dto){
        log.info("line151 memberId :{}",securityUtil.getCurrentMemberId());

        Member member = memberRepository.findByIdOrThrow(dto.getMemberId());
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(dto.getLaunchedProjectId());

        // member와 Project 기준으로 scrap 찾음
        Optional<LaunchedProjectScrap> existingScrap = launchedProjectScrapRepository.findByMemberAndLaunchedProject(member, launchedProject);

        ToggleStatus toggleStatus = ToggleStatus.FALSE;

        LaunchedProjectScrap scrap = null;

        if(existingScrap.isPresent()){
            // 만약 해당 글에 이미 scrap을 누른 회원이라면 scrap 삭제 처리
            scrap = existingScrap.get();
            launchedProjectScrapRepository.delete(scrap);
            launchedProjectScrapRepository.flush(); // 즉시 반영
        }else{
            // 해당 프로젝트 글에 scrap을 누른 회원이 아니라면 scrap 추가 처리, toggle상태 = true
            scrap = dto.toEntity(dto, member, launchedProject);
//            scrapResponse = LaunchedProjectScrap.scrapResfromEntity(launchedProject,scrap,ToggleStatus.TRUE);
            launchedProjectScrapRepository.save(scrap);
            toggleStatus = ToggleStatus.TRUE;
        }
        // 카운트 구하기
        int scrapCount = launchedProject.getLaunchedProjectScraps().size();

        return LaunchedProjectScrapResponse.builder()
                .launchedProjectId(dto.getLaunchedProjectId())
                .scrapCount(scrapCount)
                .toggleStatus(toggleStatus)
                .build();
    }

    // READ
    // Launched-Project의 id를 기준으로 LaunchedProject의 BasicInfo 조회
    public BasicInfoLaunchedProjectResponse getBasicInfo(Long id) {
        LaunchedProject launchedProject = launchedProjectRepository.findByIdOrThrow(id);
        BasicInfoLaunchedProjectResponse basicInfoDto = launchedProject.BasicInfoResfromEntity(launchedProject);
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
        Page<LaunchedProject> launchedProjects = launchedProjectRepository.findAll(pageable);
        Page<ListLaunchedProjectResponse> listDtoPage = launchedProjects.map(LaunchedProject::listResFromEntity);

        return listDtoPage;
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

}

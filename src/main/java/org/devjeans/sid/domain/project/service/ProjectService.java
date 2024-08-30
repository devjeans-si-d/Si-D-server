package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.chatRoom.dto.sse.SseTeamBuildResponse;
import org.devjeans.sid.domain.chatRoom.service.SseService;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.ApplicantsCountResDto;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.dto.scrap.ScrapResponse;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectRequest;

import org.devjeans.sid.domain.project.entity.*;

import org.devjeans.sid.domain.project.dto.update.UpdateProjectResponse;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.*;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.*;

//Todo ResponseEntity() 적용
@Service
@Transactional
public class ProjectService {
    // 나머지 repository는 필요할 때 가져오기!!!!!
    private final ProjectRepository projectRepository;
    private final ProjectApplicationRepository projectApplicationRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitInfoRepository recruitInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> scrapRedisTemplate;
    private final SseService sseService;

    private final SecurityUtil securityUtil;
    private final ProjectScrapRepository projectScrapRepository;
    private static final String VIEWS_KEY_PREFIX = "project_views:";
    private static final String SCRAP_KEY_PREFIX = "project_scraps:";
    private static final String MEMBER_SCRAP_LIST = "member_scrap_list:";


    @Autowired
    public ProjectService(@Qualifier("scrapRedisTemplate") RedisTemplate<String, Object> scrapRedisTemplate,
                          ProjectRepository projectRepository,
                          ProjectApplicationRepository projectApplicationRepository,
                          MemberRepository memberRepository,
                          ProjectMemberRepository projectMemberRepository,
                          RecruitInfoRepository recruitInfoRepository,
                          @Qualifier("viewRedisTemplate") RedisTemplate<String, String> redisTemplate,
                          SecurityUtil securityUtil,
                          ProjectScrapRepository projectScrapRepository,
                          SseService sseService) {
        this.projectRepository = projectRepository;
        this.projectApplicationRepository = projectApplicationRepository;
        this.memberRepository = memberRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.recruitInfoRepository = recruitInfoRepository;
        this.scrapRedisTemplate = scrapRedisTemplate;
        this.redisTemplate = redisTemplate;

        this.securityUtil = securityUtil;
        this.projectScrapRepository = projectScrapRepository;
        this.sseService = sseService;
    }

    public String updateIsClosed(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        if(project.getIsClosed().equals("N")) {
            project.setIsClosed("Y");
        } else {
            throw new BaseException(PROJECT_ALREADY_CLOSED);
        }

        //== SSE ==//
        List<ProjectMember> projectMembers = project.getProjectMembers();
        for (ProjectMember projectMember : projectMembers) {
            SseTeamBuildResponse sseTeamBuildResponse = new SseTeamBuildResponse(project.getId(), project.getProjectName(), project.getPm().getId());
            sseService.sendTeamBuild(projectMember.getMember().getId(), sseTeamBuildResponse, projectMembers);
        }

        return "project 마감 여부 :" + project.getIsClosed();
    }


    //create
    public Project projectCreate(CreateProjectRequest dto) {
        Member pm = memberRepository.findById(securityUtil.getCurrentMemberId()).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));

        Project project = dto.toEntity(pm);
        //create시 projectMember에 pm 자동 add
        ProjectMember pmToAddProjectMember = ProjectMember.builder().member(pm).jobField(JobField.PM).project(project).build();
        project.getProjectMembers().add(pmToAddProjectMember);


        for (CreateProjectRequest.RecruitInfoCreateRequest recruitInfoInput : dto.getRecruitInfos()) {
            // jobfield, count, project
            RecruitInfo recruitInfo = RecruitInfo.builder()
                    .jobField(recruitInfoInput.getJobField())
                    .project(project)
                    .count(recruitInfoInput.getCount())
                    .build();
            project.getRecruitInfos().add(recruitInfo);
        }

        Project savedProject = projectRepository.save(project);

        return savedProject;

    }

    // read-detail
    public DetailProjectResponse projectReadDetail(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        incrementViews(id);

        // view redis 가져오기
        String viewKey = VIEWS_KEY_PREFIX + project.getId();
        String views = redisTemplate.opsForValue().get(viewKey);
        Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

        // scrap redis 가져오기
        String projectKey = "project_scrap_count:" + project.getId();
        ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
        Object count = valueOperations.get(projectKey);
        Long scrapCount = count != null ? Long.valueOf(count.toString()) : 0L;
//        Boolean isScrap;
        // member scrap 체크 -> 이 부분을 따로 빼기 scrap 확인 api 따로 빼기
//        if(securityUtil.isMember().equals(true)) {
//            String memberKey = MEMBER_SCRAP_LIST + securityUtil.getCurrentMemberId();
//            SetOperations<String, Object> setOperations = scrapRedisTemplate.opsForSet();
//            isScrap = setOperations.isMember(memberKey, project.getId());
//        }
//        else isScrap=false;

        ApplicantsCountResDto applicantsCountResDto = new ApplicantsCountResDto();
        // jobfield별 모집자 수
        List<Object[]> results = projectApplicationRepository.countApplicationsByJobFieldAndProjectId(project.getId());
        for (Object[] result : results) {
            JobField jobField = (JobField) result[0];
            Long countApply = (Long) result[1];
            if(jobField == JobField.BACKEND) applicantsCountResDto.setBACKEND(countApply);
            if(jobField == JobField.FRONTEND) applicantsCountResDto.setFRONTEND(countApply);
            if(jobField == JobField.APP) applicantsCountResDto.setAPP(countApply);
            if(jobField == JobField.DESIGNER) applicantsCountResDto.setDESIGNER(countApply);
        }
        return DetailProjectResponse.fromEntity(project, scrapCount, viewCount, applicantsCountResDto);
    }




    // read-list
    public Page<ListProjectResponse> projectReadAll(Pageable pageable) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
        Page<Project> projectList = projectRepository.findByIsClosedAndDeletedAtIsNullOrderByUpdatedAtDesc(pageable, "N");
        for (Project project : projectList) {
            // view redis 가져오기
            String viewKey = VIEWS_KEY_PREFIX + project.getId();
            String views = redisTemplate.opsForValue().get(viewKey);
            Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

            // scrap redis 가져오기
            String projectKey = "project_scrap_count:" + project.getId();
            ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
            Object count = valueOperations.get(projectKey);
            Long scrapCount = count != null ? Long.valueOf(count.toString()) : 0L;


            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
                    .views(viewCount)
                    .imageUrl(project.getImageUrl())
                    .scrapCount(scrapCount)
                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);


        }
        return new PageImpl<>(listProjectResponses, pageable, projectList.getTotalElements());
    }


    // update project
    public UpdateProjectResponse projectUpdate(UpdateProjectRequest dto, Long projectId) {
        // Todo : dto랑 실제 고치려는 프로젝트랑 체크해서 다르면 error 날려야하는데 아직 unique값이 없어서 나중에 처리

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND)); //Todo : 에러 처리 컨벤션 적용

        List<RecruitInfo> toDeleteRecruit = project.getRecruitInfos();
        for (RecruitInfo recruitInfo : toDeleteRecruit) recruitInfoRepository.delete(recruitInfo);
        List<RecruitInfo> toUpdateRecruitInfos = new ArrayList<>();
        for (UpdateProjectRequest.RecruitInfoUpdateRequest recruitInfoInput : dto.getRecruitInfos()) {
            RecruitInfo recruitInfo = RecruitInfo.builder().count(recruitInfoInput.getCount()).project(project).jobField(recruitInfoInput.getJobField()).build();
            toUpdateRecruitInfos.add(recruitInfo);
        }
        project.setRecruitInfos(toUpdateRecruitInfos);


        Project updatedProject = UpdateProjectRequest.updateProject(project, dto);

        Project toCheckUpdateProject = projectRepository.save(updatedProject);
        return UpdateProjectResponse.fromEntity(toCheckUpdateProject);
    }

    // delete project by deletedAt
    public String deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        if (!project.getPm().getId().equals(securityUtil.getCurrentMemberId()))
            throw new BaseException(UNAUTHORIZED_ACTION);
        if (project.getDeletedAt() == null) {
            project.setDeletedAt(LocalDateTime.now());
            projectRepository.save(project);
        } else {
            throw new BaseException(PROJECT_ALREADY_DELETED);
        }
        return "삭제 성공";
    }

    // 조회수 증가
    public void incrementViews(Long id) {
        String key = VIEWS_KEY_PREFIX + id;
        Long views = redisTemplate.opsForValue().increment(key, 1);
    }


    public List<ListProjectResponse> projectListReadIsClosedN(String sorted) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
        List<Project> projectList = projectRepository.findByIsClosedAndDeletedAtIsNullOrderByCreatedAtDesc("N");
        for (Project project : projectList) {
            // view redis 가져오기
            String viewKey = VIEWS_KEY_PREFIX + project.getId();
            String views = redisTemplate.opsForValue().get(viewKey);
            Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

            // scrap redis 가져오기
            String projectKey = "project_scrap_count:" + project.getId();
            ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
            Object count = valueOperations.get(projectKey);
            Long scrapCount = count != null ? Long.valueOf(count.toString()) : 0L;

            List<ListProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
            for( RecruitInfo recruitInfo :project.getRecruitInfos()){
                ListProjectResponse.RecruitInfoDto recruitInfoDto = ListProjectResponse.RecruitInfoDto.builder()
                        .id(recruitInfo.getId())
                        .count(recruitInfo.getCount())
                        .jobField(recruitInfo.getJobField())
                        .build();
                recruitInfoDtos.add(recruitInfoDto);
            }

            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
//                    .isScrap(isScrap)
                    .views(viewCount)
                    .imageUrl(project.getImageUrl())
                    .scrapCount(scrapCount)
                    .recruitInfos(recruitInfoDtos)

                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);

        }
        if( "views".equals(sorted)) listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getViews).reversed());
        else if("scraps".equals(sorted))    listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getScrapCount).reversed());

        return listProjectResponses;
    }

    public List<ListProjectResponse> projectListReadAll(String sorted) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
        List<Project> projectList = projectRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
        for (Project project : projectList) {
            // view redis 가져오기
            String viewKey = VIEWS_KEY_PREFIX + project.getId();
            String views = redisTemplate.opsForValue().get(viewKey);
            Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

            // scrap redis 가져오기
            String projectKey = "project_scrap_count:" + project.getId();
            ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
            Object count = valueOperations.get(projectKey);
            Long scrapCount = count != null ? Long.valueOf(count.toString()) : 0L;

            List<ListProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
            for( RecruitInfo recruitInfo :project.getRecruitInfos()){
                ListProjectResponse.RecruitInfoDto recruitInfoDto = ListProjectResponse.RecruitInfoDto.builder()
                        .id(recruitInfo.getId())
                        .count(recruitInfo.getCount())
                        .jobField(recruitInfo.getJobField())
                        .build();
                recruitInfoDtos.add(recruitInfoDto);
            }

            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
//                    .isScrap(isScrap)
                    .views(viewCount)
                    .recruitInfos(recruitInfoDtos)
                    .imageUrl(project.getImageUrl())
                    .scrapCount(scrapCount)
                    .isClosed(project.getIsClosed())

                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);

        }
        if( "views".equals(sorted)) listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getViews).reversed());
        else if("scraps".equals(sorted))    listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getScrapCount).reversed());

        return listProjectResponses;
    }

    public List<ListProjectResponse> projectListReadAllNotMember(String sorted) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
        List<Project> projectList = projectRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
        for (Project project : projectList) {
            List<ListProjectResponse.RecruitInfoDto> recruitInfoDtos = new ArrayList<>();
            for( RecruitInfo recruitInfo :project.getRecruitInfos()){
                ListProjectResponse.RecruitInfoDto recruitInfoDto = ListProjectResponse.RecruitInfoDto.builder()
                        .id(recruitInfo.getId())
                        .count(recruitInfo.getCount())
                        .jobField(recruitInfo.getJobField())
                        .build();
                recruitInfoDtos.add(recruitInfoDto);
            }

            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
//                    .isScrap(isScrap)
                    .views(project.getViews())
                    .recruitInfos(recruitInfoDtos)
                    .imageUrl(project.getImageUrl())
                    .scrapCount(project.getScrapCount())
                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);

        }
        if( "views".equals(sorted)) listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getViews).reversed());
        else if("scraps".equals(sorted))    listProjectResponses.sort(Comparator.comparing(ListProjectResponse::getScrapCount).reversed());

        return listProjectResponses;
    }
}

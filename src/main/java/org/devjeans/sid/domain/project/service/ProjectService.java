package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.dto.scrap.ScrapResponse;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectRequest;

import org.devjeans.sid.domain.project.entity.*;

import org.devjeans.sid.domain.project.dto.update.UpdateProjectResponse;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.project.repository.ProjectScrapRepository;
import org.devjeans.sid.domain.project.repository.RecruitInfoRepository;
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
import java.util.List;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.*;

//Todo ResponseEntity() 적용
@Service
@Transactional
public class ProjectService {
    // 나머지 repository는 필요할 때 가져오기!!!!!
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitInfoRepository recruitInfoRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final RedisTemplate<String, Object> scrapRedisTemplate;

    private final SecurityUtil securityUtil;
    private final ProjectScrapRepository projectScrapRepository;
    private static final String VIEWS_KEY_PREFIX="project_views:";
    private static final String SCRAP_KEY_PREFIX="project_scraps:";


    @Autowired
    public ProjectService(@Qualifier("scrapRedisTemplate") RedisTemplate<String, Object> scrapRedisTemplate,ProjectRepository projectRepository, MemberRepository memberRepository, ProjectMemberRepository projectMemberRepository, RecruitInfoRepository recruitInfoRepository,@Qualifier("viewRedisTemplate")RedisTemplate<String, String> redisTemplate, SecurityUtil securityUtil, ProjectScrapRepository projectScrapRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.recruitInfoRepository = recruitInfoRepository;
        this.scrapRedisTemplate = scrapRedisTemplate;
        this.redisTemplate = redisTemplate;

        this.securityUtil = securityUtil;
        this.projectScrapRepository = projectScrapRepository;
    }
    //create
    // Todo : imageurl multipart
    public Project projectCreate(CreateProjectRequest dto){
//        Member pm = memberRepository.findById(dto.getPmId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
//        Member pm = memberRepository.findById(dto.getPmId()).orElseThrow(()->new BaseException(MEMBER_NOT_FOUND));
        Member pm = memberRepository.findById(securityUtil.getCurrentMemberId()).orElseThrow(()->new BaseException(MEMBER_NOT_FOUND));

        // Todo : s3 프론트엔드 업로드하면 string, 현재는 테스트를 위해 파일 입력 코드 필요
            Project project = dto.toEntity(pm);
            // Todo : path 추후 변경 필요
            //create시 projectMember에 pm 자동 add
            ProjectMember pmToAddProjectMember = ProjectMember.builder().member(pm).jobField(JobField.PM).project(project).build();
            project.getProjectMembers().add(pmToAddProjectMember);

            for (CreateProjectRequest.ProjectMemberCreateRequest inputMember : dto.getProjectMembers()) {
                Member memberInput = memberRepository.findById(inputMember.getMemberId()).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
                ProjectMember projectMember = ProjectMember.builder().member(memberInput).project(project).jobField(inputMember.getJobField()).build();
                project.getProjectMembers().add(projectMember);
            }

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
    // Todo :  deletedAt==null 인 것만 조회
    public DetailProjectResponse projectReadDetail(Long id){
        // Todo 에러처리
        // Todo 추후 isclosed 체크
        Project project = projectRepository.findById(id).orElseThrow(()->new BaseException(PROJECT_NOT_FOUND));
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


        return DetailProjectResponse.fromEntity(project,scrapCount,viewCount);
    }

    // read-list
    // Todo page 적용 & deletedAt==null 인 것만 조회
    public Page<ListProjectResponse> projectReadAll(Pageable pageable){
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
//        Page<Project> projectList = projectRepository.findAll(pageable);
//        Page<Project> projectList = projectRepository.findByIsClosedAndDeletedAtIsNullOrderByUpdatedAtDesc(pageable,"N");
        Page<Project> projectList = projectRepository.findByDeletedAtIsNullOrderByUpdatedAtDesc(pageable);
        //Todo isClosed=N, deletedAt is null, orderby updatedAt repository 구현
//        Page<Project> projectList = projectRepository.findActiveNotDeletedProjectsOrderByUpdatedAt(pageable,"N");
        for(Project project : projectList){
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
                    .projectName(project.getProjectName())
                    .views(viewCount)
                    .scrapCount(scrapCount)
                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);



        }
//        return listProjectResponses;
        return new PageImpl<>(listProjectResponses,pageable,projectList.getTotalElements());
    }

    // update project
    // Todo : imageurl multipart
    public UpdateProjectResponse projectUpdate(UpdateProjectRequest dto,Long projectId) {
        // Todo : dto랑 실제 고치려는 프로젝트랑 체크해서 다르면 error 날려야하는데 아직 unique값이 없어서 나중에 처리

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND)); //Todo : 에러 처리 컨벤션 적용
        List<ProjectMember> toDelete = project.getProjectMembers();
        for (ProjectMember member : toDelete) {
            projectMemberRepository.delete(member);
        }
        List<ProjectMember> toUpdateProjectMember = new ArrayList<>();
        for (UpdateProjectRequest.ProjectMemberUpdateRequest inputMember : dto.getProjectMembers()) {
            Member memberInput = memberRepository.findById(inputMember.getMemberId()).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
            ProjectMember projectMember = ProjectMember.builder().member(memberInput).project(project).jobField(inputMember.getJobField()).build();
            toUpdateProjectMember.add(projectMember);
        }
        project.setProjectMembers(toUpdateProjectMember);

        List<RecruitInfo> toDeleteRecruit = project.getRecruitInfos();
        for (RecruitInfo recruitInfo : toDeleteRecruit) recruitInfoRepository.delete(recruitInfo);
        List<RecruitInfo> toUpdateRecruitInfos = new ArrayList<>();
        for (UpdateProjectRequest.RecruitInfoUpdateRequest recruitInfoInput : dto.getRecruitInfos()) {
            RecruitInfo recruitInfo = RecruitInfo.builder().count(recruitInfoInput.getCount()).project(project).jobField(recruitInfoInput.getJobField()).build();
            toUpdateRecruitInfos.add(recruitInfo);
        }
        project.setRecruitInfos(toUpdateRecruitInfos);


        Project updatedProject=UpdateProjectRequest.updateProject(project,dto);

        Project toCheckUpdateProject = projectRepository.save(updatedProject);
        return UpdateProjectResponse.fromEntity(toCheckUpdateProject);
    }

    // delete project by deletedAt
    // Todo projectid 연동된 projectmember, recruitmember 같이 삭제
    public String deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new BaseException(PROJECT_NOT_FOUND));
        if(!project.getPm().getId().equals(securityUtil.getCurrentMemberId())) throw new BaseException(UNAUTHORIZED_ACTION);
        if (project.getDeletedAt()==null) {
            project.setDeletedAt(LocalDateTime.now());
            projectRepository.save(project);
        } else {
            throw new BaseException(PROJECT_ALREADY_DELETED);
        }
        return "삭제 성공";
    }

    // ProjectScrap 생성
    public ScrapResponse createScrap(Long id){
        Long memberId = securityUtil.getCurrentMemberId();
        SetOperations<String,String> scrapRedis = redisTemplate.opsForSet();
        Long check=scrapRedis.add("scrap key: "+id.toString(),"scrap value: "+memberId.toString());
        System.out.println("check: "+check+ ", size: "+scrapRedis.size("scrap key: "+id.toString())+"key:"+scrapRedis.members(id.toString()));




        Member member = memberRepository.findById(securityUtil.getCurrentMemberId()).orElseThrow(()->new BaseException(MEMBER_NOT_FOUND));
        Project project = projectRepository.findById(id).orElseThrow(()->new BaseException(PROJECT_NOT_FOUND));


        ScrapResponse scrapResponse = ScrapResponse.builder().memberId(member.getId()).projectId(id).build();

        projectScrapRepository.save(ScrapResponse.toEntity(project, member));
        // Todo : scrap create 원래 있는지 체크 있으면 에러
        // Todo : scrap create 시 redis 보내기
        return scrapResponse;
    }

    // 해당 프로젝트의 projectScrap 리스트
    public Page<ScrapResponse> projectScrap(Long id, Pageable pageable){
        Page<ProjectScrap> projectScraps = projectScrapRepository.findByProjectId(id,pageable);
        List<ScrapResponse> scrapResponseList= new ArrayList<>();
        for(ProjectScrap projectScrap: projectScraps){
            scrapResponseList.add(ScrapResponse.builder()
                    .memberId(projectScrap.getMember().getId())
                    .projectId(projectScrap.getProject().getId())
                    .build());
        }
        return new PageImpl<>(scrapResponseList,pageable,projectScraps.getTotalElements());
    }

    // 사용중인 유저의 project scrap list
    public Page<ScrapResponse> myProjectScrap(Pageable pageable){
        Page<ProjectScrap> projectScraps = projectScrapRepository.findByMemberId(securityUtil.getCurrentMemberId(),pageable);
        List<ScrapResponse> scrapResponseList= new ArrayList<>();
        for(ProjectScrap projectScrap: projectScraps){
            scrapResponseList.add(ScrapResponse.builder()
                    .memberId(projectScrap.getMember().getId())
                    .projectId(projectScrap.getProject().getId())
                    .build());
        }
        return new PageImpl<>(scrapResponseList,pageable,projectScraps.getTotalElements());
    }

    // project scrap delete
    public String projectDeleteScrap(Long id) {
        Member member = memberRepository.findById(securityUtil.getCurrentMemberId()).orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        Project project = projectRepository.findById(id).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        ProjectScrap projectScrap = projectScrapRepository.findByProjectIdAndMemberId(project.getId(), member.getId());
        // Todo : 없으면 에러 처리
        // Todo : redis 감소 시키기
        projectScrapRepository.delete(projectScrap);
        return "삭제 완료";
    }

    // 조회수 증가
    public void incrementViews(Long id){
        String key = VIEWS_KEY_PREFIX + id;
        Long views = redisTemplate.opsForValue().increment(key,1);

    }
}

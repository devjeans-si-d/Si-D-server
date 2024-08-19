package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.dto.scrap.ScrapResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.*;
import static org.devjeans.sid.global.exception.exceptionType.ScrapProjectException.ALREADY_SCRAP_PROJECT;
import static org.devjeans.sid.global.exception.exceptionType.ScrapProjectException.SCRAP_PROJECT_NOT_FOUND;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScrapService {

    private static final String PROJECT_SCRAP_COUNT = "project_scrap_count:";
    private static final String MEMBER_SCRAP_LIST = "member_scrap_list:";
    private final SecurityUtil securityUtil;
    private final RedisTemplate<String, Object> scrapRedisTemplate;
    private final ProjectRepository projectRepository;
    private static final String VIEWS_KEY_PREFIX="project_views:";
    private final RedisTemplate<String,String> viewRedisTemplate;

    @Autowired
    public ScrapService(SecurityUtil securityUtil, @Qualifier("scrapRedisTemplate") RedisTemplate<String, Object> scrapRedisTemplate, ProjectRepository projectRepository, @Qualifier("viewRedisTemplate")RedisTemplate<String, String> viewRedisTemplate) {
        this.securityUtil = securityUtil;
        this.scrapRedisTemplate = scrapRedisTemplate;
        this.projectRepository = projectRepository;
        this.viewRedisTemplate = viewRedisTemplate;
    }

    public ScrapResponse scrapProject(Long projectId) {
        Long memberId = securityUtil.getCurrentMemberId();
        ScrapResponse response = null;
        // member id - 프로젝트 id (set임)
        // member 먼저 해야 이미 스크랩한 사람이면 에러처리됨
        // 멤버별 스크랩 목록에 추가 (Set)
        Project projectCheck = projectRepository.findById(projectId).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        String memberKey = MEMBER_SCRAP_LIST + memberId;
        SetOperations<String, Object> memberSrapSet = scrapRedisTemplate.opsForSet();
        if (memberSrapSet.isMember(memberKey, String.valueOf(projectId)) == true) {
            throw new BaseException(ALREADY_SCRAP_PROJECT);
        }
        memberSrapSet.add(memberKey, projectId);


        // 프로젝트 id - 스크랩 카운트 (set 아님)
        // 프로젝트별 스크랩 수 증가 (String)
        String projectKey = PROJECT_SCRAP_COUNT + projectId;
        ValueOperations<String, Object> projectScrapCount = scrapRedisTemplate.opsForValue();
        projectScrapCount.increment(projectKey, 1);
        if (memberSrapSet.isMember(memberKey, String.valueOf(projectId)) == true) {
            response = new ScrapResponse(memberId, projectId);
        }

        return response;
    }

    public ScrapResponse unscrapProject(Long projectId){
        ScrapResponse scrapResponse = null;
        Long memberId = securityUtil.getCurrentMemberId();
        Project projectCheck = projectRepository.findById(projectId).orElseThrow(() -> new BaseException(PROJECT_NOT_FOUND));
        String memberKey = MEMBER_SCRAP_LIST + memberId;

        //멤버별 스크랩 목록에 삭제
        SetOperations<String, Object> memberSrapSet = scrapRedisTemplate.opsForSet();
        if(memberSrapSet.isMember(memberKey,projectId.toString())==false) throw new BaseException(SCRAP_PROJECT_NOT_FOUND);
        memberSrapSet.remove(memberKey,projectId);

        // 프로젝트 스크랩수 count 감소
        String projectKey = PROJECT_SCRAP_COUNT + projectId;
        ValueOperations<String, Object> projectScrapCount = scrapRedisTemplate.opsForValue();
        projectScrapCount.decrement(projectKey, 1);
        if (memberSrapSet.isMember(memberKey, String.valueOf(projectId)) == false) {
            scrapResponse = new ScrapResponse(memberId, projectId);
        }
        return scrapResponse;
    }

    public Long getProjectScrapCount(Long projectId) {
        Long response = 0L;
        String projectKey = PROJECT_SCRAP_COUNT + projectId;
        ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
        Object count = valueOperations.get(projectKey);
        if(count!=null) response=Long.parseLong(count.toString());
        return response;
    }

    public Set<Object> getMemberScrapList() {
        String memberKey = MEMBER_SCRAP_LIST + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = scrapRedisTemplate.opsForSet();
        return setOperations.members(memberKey);
    }

    public Page<ListProjectResponse> getMemberScrapProjectList(Pageable pageable) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();

        String memberKey = MEMBER_SCRAP_LIST + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = scrapRedisTemplate.opsForSet();
        Set<Object> scrapSet = setOperations.members(memberKey);
        List<Long> projectIdList = scrapSet.stream()
                .map(object -> Long.valueOf(object.toString())) // Set의 Object를 Long으로 변환
                .collect(Collectors.toList());

        // projectIdList를 역순으로 정렬
        Collections.sort(projectIdList, Comparator.reverseOrder());


        // 페이징을 위한 인덱스 계산
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), projectIdList.size());
        // 페이지 범위 내에서만 가져오기
        List<Long> pagedProjectIdList = projectIdList.subList(start, end);

        // Set<Object>을 List<Long>으로 변환
        List<Project> projectList = pagedProjectIdList.stream().map((id)->projectRepository.findById(id).orElseThrow(()->new BaseException(PROJECT_NOT_FOUND))).collect(Collectors.toList());
        for(Project project : projectList){
            // 조회수
            String viewKey = VIEWS_KEY_PREFIX + project.getId();
            String views = viewRedisTemplate.opsForValue().get(viewKey);
            Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

            // 스크랩
            String projectKey = PROJECT_SCRAP_COUNT + project.getId();
            ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
            Object count = valueOperations.get(projectKey);
            Long scrapCount = count != null ? Long.valueOf(count.toString()) : 0L;
            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .id(project.getId())
                    .imageUrl(project.getImageUrl())
                    .projectName(project.getProjectName())
                    .views(viewCount)
                    .scrapCount(scrapCount)
                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);
        }
        return new PageImpl<>(listProjectResponses, pageable, projectIdList.size());

    }

    public boolean isProjectScrappedByMember(Long projectId) {
        String memberKey = MEMBER_SCRAP_LIST + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = scrapRedisTemplate.opsForSet();
        return Boolean.TRUE.equals(setOperations.isMember(memberKey, projectId));
    }
}
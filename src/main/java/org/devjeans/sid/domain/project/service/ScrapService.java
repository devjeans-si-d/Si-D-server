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

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapService {

    private static final String SCRAP_COUNT_KEY_PREFIX = "project_scrap_count:";
    private static final String MEMBER_SCRAP_LIST_KEY_PREFIX = "member_scrap_list:";
    private final SecurityUtil securityUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProjectRepository projectRepository;
    private static final String VIEWS_KEY_PREFIX="project_views:";
    private final RedisTemplate<String,String> viewRedisTemplate;

    @Autowired
    public ScrapService(SecurityUtil securityUtil, @Qualifier("scrapRedisTemplate") RedisTemplate<String, Object> redisTemplate, ProjectRepository projectRepository, @Qualifier("viewRedisTemplate")RedisTemplate<String, String> viewRedisTemplate) {
        this.securityUtil = securityUtil;
        this.redisTemplate = redisTemplate;
        this.projectRepository = projectRepository;
        this.viewRedisTemplate = viewRedisTemplate;
    }

    public ScrapResponse scrapProject(Long projectId) {
        Long memberId = securityUtil.getCurrentMemberId();
        // 이미 있으면 error 처리
        if(isProjectScrappedByMember(projectId)) throw new BaseException(ALREADY_SCRAP_PROJECT);

        String projectKey = SCRAP_COUNT_KEY_PREFIX + projectId;
        String memberKey = MEMBER_SCRAP_LIST_KEY_PREFIX + memberId;

        // 프로젝트별 스크랩 수 증가 (String)
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(projectKey, 1);

        // 멤버별 스크랩 목록에 추가 (Set)
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(memberKey, projectId);

        // ScrapResponse 객체 생성 및 반환
        return new ScrapResponse(memberId, projectId);
    }

    public ScrapResponse unscrapProject(Long projectId) {
        Long memberId = securityUtil.getCurrentMemberId();
        // 스크랩되지 않았으면 에러 처리
        if(!isProjectScrappedByMember(projectId)) throw new BaseException(SCRAP_PROJECT_NOT_FOUND);

        String projectKey = SCRAP_COUNT_KEY_PREFIX + projectId;
        String memberKey = MEMBER_SCRAP_LIST_KEY_PREFIX + memberId;

        // 프로젝트별 스크랩 수 감소 (String)
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(projectKey, -1);

        // 멤버별 스크랩 목록에서 제거 (Set)
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(memberKey, projectId);

        // ScrapResponse 객체 생성 및 반환
        return new ScrapResponse(memberId, projectId);
    }

    public Long getProjectScrapCount(Long projectId) {
        String projectKey = SCRAP_COUNT_KEY_PREFIX + projectId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object count = valueOperations.get(projectKey);
        return count != null ? Long.valueOf(count.toString()) : 0L;
    }

    public Set<Object> getMemberScrapList() {
        String memberKey = MEMBER_SCRAP_LIST_KEY_PREFIX + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.members(memberKey);
    }

    public Page<ListProjectResponse> getMemberScrapProjectList(Pageable pageable) {
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();

        String memberKey = MEMBER_SCRAP_LIST_KEY_PREFIX + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        Set<Object> scrapSet = setOperations.members(memberKey);
        List<Long> projectIdList = scrapSet.stream()
                .map(object -> Long.valueOf(object.toString())) // Set의 Object를 Long으로 변환
                .collect(Collectors.toList());
        // Set<Object>을 List<Long>으로 변환
        List<Project> projectList = projectIdList.stream().map((id)->projectRepository.findById(id).orElseThrow(()->new BaseException(PROJECT_NOT_FOUND))).collect(Collectors.toList());
        for(Project project : projectList){
            // 조회수
            String viewKey = VIEWS_KEY_PREFIX + project.getId();
            String views = viewRedisTemplate.opsForValue().get(viewKey);
            Long viewCount = (views != null) ? Long.parseLong(views) : 0L;

            String projectKey = "project_scrap_count:" + project.getId();
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
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
        return new PageImpl<>(listProjectResponses,pageable,projectList.size());

    }

    public boolean isProjectScrappedByMember(Long projectId) {
        String memberKey = MEMBER_SCRAP_LIST_KEY_PREFIX + securityUtil.getCurrentMemberId();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return Boolean.TRUE.equals(setOperations.isMember(memberKey, projectId));
    }
}
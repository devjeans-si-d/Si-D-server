package org.devjeans.sid.domain.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.project.dto.scrap.ScrapResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType.PROJECT_NOT_FOUND;

@Component
public class ProjectScheduler {
    private static final String VIEWS_KEY_PREFIX="project_views:";
    private static final String SCRAP_COUNT_KEY_PREFIX = "project_scrap_count:";

    private final ProjectRepository projectRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final RedisTemplate<String, Object> scrapRedisTemplate;
    @Autowired
    public ProjectScheduler(@Qualifier("scrapRedisTemplate") RedisTemplate<String, Object> scrapRedisTemplate,ProjectRepository projectRepository, @Qualifier("viewRedisTemplate")RedisTemplate<String, String> redisTemplate) {
        this.projectRepository = projectRepository;
        this.redisTemplate = redisTemplate;
        this.scrapRedisTemplate =scrapRedisTemplate;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void projectSchedule(){

        Page<Project> projects = projectRepository.findByIsClosed(Pageable.unpaged(),"N");
        for(Project p : projects){
            if(p.getDeadline().isBefore(LocalDateTime.now())){
                p.updateIsClosed("Y");
                projectRepository.save(p);
            }
        }
    }

    @Qualifier("viewRedisTemplate")
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void syncViews(){
        for(Project p : projectRepository.findAll()){
            // 조회수 저장
            String key = VIEWS_KEY_PREFIX+p.getId();
            String value = redisTemplate.opsForValue().get(key);
            Long view = 0L;
            if(value!=null) view = Long.parseLong(value);
            p.setViews(view);
            projectRepository.save(p);
        }
    }


    @Qualifier("scrapRedisTemplate")
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void syncScraps(){
        for(Project p : projectRepository.findAll()){
            // scrap 저장
            String projectKey = SCRAP_COUNT_KEY_PREFIX + p.getId();
            ValueOperations<String, Object> valueOperations = scrapRedisTemplate.opsForValue();
            Object count = valueOperations.get(projectKey);
            Long views = count != null ? Long.valueOf(count.toString()) : 0L;
            p.setScrapCount(views);
            projectRepository.save(p);
        }
    }
}

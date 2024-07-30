package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ProjectScheduler {
    private static final String VIEWS_KEY_PREFIX="project_views:";

    private final ProjectRepository projectRepository;
    private final RedisTemplate<String,String> redisTemplate;
    @Autowired
    public ProjectScheduler(ProjectRepository projectRepository, RedisTemplate<String, String> redisTemplate) {
        this.projectRepository = projectRepository;
        this.redisTemplate = redisTemplate;
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

    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void syncViews(){
        for(Project p : projectRepository.findAll()){
            String key = VIEWS_KEY_PREFIX+p.getId();
            String value = redisTemplate.opsForValue().get(key);
            Long view = 0L;
            if(value!=null) view = Long.parseLong(value);
            p.setViews(view);
            projectRepository.save(p);
        }
    }
}

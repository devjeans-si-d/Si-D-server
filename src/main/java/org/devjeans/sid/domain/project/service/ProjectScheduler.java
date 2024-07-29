package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ProjectScheduler {
    private final ProjectRepository projectRepository;
    @Autowired
    public ProjectScheduler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
}

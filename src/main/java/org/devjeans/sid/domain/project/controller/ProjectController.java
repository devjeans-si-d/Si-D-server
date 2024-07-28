package org.devjeans.sid.domain.project.controller;

import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.dto.create.CreateProjectResponse;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectRequest;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // create
    @PostMapping("/api/project/create")
    public String projectCreatePost(@RequestBody CreateProjectRequest createProjectRequest){
        Project project= projectService.projectCreate(createProjectRequest);
       // Todo return 값 컨벤션 맞추기
        return project.getProjectName();
    }

    // read - list
    // Todo pageable 적용하기
    // Todo return 값 컨벤션 맞추기
    @GetMapping("/api/project/list")
    public List<ListProjectResponse> projectListGet(){
        return projectService.projectReadAll();
    }

    // read - detail
    // Todo return 값 컨벤션 맞추기
    @GetMapping("/api/project/{id}")
    public DetailProjectResponse projectDetailGet(@PathVariable Long id){
        return projectService.projectReadDetail(id);
    }

    // update
    // Todo return 값 컨벤션 맞추기
    @PutMapping("/api/project/{id}/update")
    public UpdateProjectResponse projectUpdatePut(@RequestBody UpdateProjectRequest updateProjectRequest, @PathVariable Long id){
        return projectService.projectUpdate(updateProjectRequest,id);
    }

    // delete
    // Todo return 값 컨벤션 맞추기
    @DeleteMapping("/api/project/{id}")
    public void projectDelete(@PathVariable Long id) {
        projectService.deleteProject(id);
    }



}

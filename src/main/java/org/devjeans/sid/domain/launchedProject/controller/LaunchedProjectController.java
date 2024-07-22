package org.devjeans.sid.domain.launchedProject.controller;

import org.devjeans.sid.domain.launchedProject.dto.CreateLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.DetailLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.ListLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.service.LaunchedProjectService;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/launched-projects")
public class LaunchedProjectController {

    private final LaunchedProjectService launchedProjectService;

    @Autowired
    public LaunchedProjectController(LaunchedProjectService launchedProjectService){
        this.launchedProjectService = launchedProjectService;
    }

    // 유저는 프로젝트 완성글을 작성할 수 있다.
    @PostMapping("/create")
    public void launchedProjectCreate(@RequestBody CreateLaunchedProjectRequest createLaunchedProjectRequest){

    }

    // 유저는 런칭된 프로젝트 디테일 페이지를 조회할 수 있다.
    @GetMapping("/{id}")
    public ResponseEntity<DetailLaunchedProjectResponse> LauchedProjectDetail(@PathVariable Long id){
        LaunchedProject launchedProject = launchedProjectService.findById(id)
                .orElaseThrow(()-> new BaseException(ExceptionType.PROJECT_NOT_FOUND));

        DetailLaunchedProjectResponse detailLaunchedProjectResponse = DetailLaunchedProjectResponse.fromEntity(launchedProject);
        return ResponseEntity.ok(detailLaunchedProjectResponse);
    }

    // 유저는 프로젝트 모집글을 삭제할 수 있다.


    // 유저는 런칭된 프로젝트 목록을 조회할 수 있다.
//    @GetMapping("/launched-project/list")
//    public List<ListLaunchedProjectResponse> launchedProjectResponseList


    // 유저는 프로젝트 완성글에 사이다를 누를 수 있다.

}

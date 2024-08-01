package org.devjeans.sid.domain.mainPage.controller;

import org.devjeans.sid.domain.mainPage.dto.TopListLaunchedProjectResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListMemberResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListProjectResponse;
import org.devjeans.sid.domain.mainPage.service.MainPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
public class MainPageController {

    @Autowired
    private MainPageService mainPageService;

    @GetMapping("/top-project")
    public ResponseEntity<Object> getTopProject(@PageableDefault(size=4, page=0)Pageable pageable){
        Page<TopListProjectResponse> topListProjects = mainPageService.getTopProjects(pageable);
        return ResponseEntity.ok(topListProjects);
    }

    @GetMapping("/top-launched-project")
    public ResponseEntity<Page<TopListLaunchedProjectResponse>> getTopLaunchedProject(@PageableDefault(size=4, page=0)Pageable pageable){
        Page<TopListLaunchedProjectResponse> topListLaunchedProjects = mainPageService.getTopLaunchedProjects(pageable);
        return ResponseEntity.ok(topListLaunchedProjects);
    }

    @GetMapping("/top-sider-card")
    public ResponseEntity<Object> getTopSiderCard(@PageableDefault(size=3, page=0, sort="updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<TopListMemberResponse> topListMembers = mainPageService.getTopMembers(pageable);
        return ResponseEntity.ok(topListMembers);
    }
}

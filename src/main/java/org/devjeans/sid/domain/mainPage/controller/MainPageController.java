package org.devjeans.sid.domain.mainPage.controller;

import org.devjeans.sid.domain.mainPage.dto.TopListLaunchedProjectResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListMemberResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListProjectResponse;
import org.devjeans.sid.domain.mainPage.service.MainPageService;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
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
    // 조회수 순으로 8개 Page (+아직 마감/삭제되지 않은 모집공고)
    @GetMapping("/top-project")
    public ResponseEntity<Object> getTopProject(@PageableDefault(size=8, page=0)Pageable pageable){
        Page<ListProjectResponse> topListProjects = mainPageService.getTopProjects(pageable);
        return ResponseEntity.ok(topListProjects);
    }
    // 조회수 순으로 8개 Page (+아직 삭제되지 않은 완성 프로젝트 글)
    @GetMapping("/top-launched-project")
    public ResponseEntity<Page<TopListLaunchedProjectResponse>> getTopLaunchedProject(@PageableDefault(size=8, page=0)Pageable pageable){
        Page<TopListLaunchedProjectResponse> topListLaunchedProjects = mainPageService.getTopLaunchedProjects(pageable);
        return ResponseEntity.ok(topListLaunchedProjects);
    }
    // 최신회원 6명
    @GetMapping("/top-sider-card")
    public ResponseEntity<Object> getTopSiderCard(@PageableDefault(size=6, page=0, sort="updatedAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<TopListMemberResponse> topListMembers = mainPageService.getTopMembers(pageable);
        return ResponseEntity.ok(topListMembers);
    }
}

package org.devjeans.sid.domain.launchedProject.controller;

import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.BasicInfoLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.ListLaunchedProjectResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchProjectDTO.SaveLaunchedProjectRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectMemberDTO.LaunchedProjectMemberResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapRequest;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectScrapDTO.LaunchedProjectScrapResponse;
import org.devjeans.sid.domain.launchedProject.dto.LaunchedProjectTechStackDTO.LaunchedProjectTechStackResponse;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.service.LaunchedProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/launched-project")
public class LaunchedProjectController {

    private final LaunchedProjectService launchedProjectService;

    @Autowired
    public LaunchedProjectController(LaunchedProjectService launchedProjectService){
        this.launchedProjectService = launchedProjectService;
    }

    // 완성된 프로젝트 글 등록
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestPart(value="saveRequest") SaveLaunchedProjectRequest saveRequest,
                                                    @RequestPart(value="launchedProjectImage") MultipartFile launchedProjectImage){
        LaunchedProject launchedProject = launchedProjectService.register(saveRequest,launchedProjectImage);
        return ResponseEntity.ok(launchedProject.getId()); // 등록된 글 id 반환
    }

    // 유저는 프로젝트 완성글에 사이다를 누를 수 있다.
    @PostMapping("/scrap")
    public ResponseEntity<String> scrap (@RequestBody LaunchedProjectScrapRequest scrapRequest){
        String message = launchedProjectService.toggleScrap(scrapRequest);
        return ResponseEntity.ok(message);
    }

    // Launched-Project id로 Launched-Project 기본정보 조회
    @GetMapping("/detail/{id}/basic-info")
    public ResponseEntity<BasicInfoLaunchedProjectResponse> getDetailBasicInfo(@PathVariable Long id) {
        BasicInfoLaunchedProjectResponse basicInfo = launchedProjectService.getBasicInfo(id);
        return ResponseEntity.ok(basicInfo);
    }

    // Launched-Project id로 프로젝트에 사용된 기술스택(TechStack)리스트 조회
    @GetMapping("/detail/{id}/tech-stacks")
    public ResponseEntity<List<LaunchedProjectTechStackResponse>> getTechStacks(@PathVariable Long id){
        List<LaunchedProjectTechStackResponse> stacks = launchedProjectService.getTechStacks(id);
        return ResponseEntity.ok(stacks);
    }

    // Launched-Project id로 프로젝트에 참여한 회원(LaunchedProjectMember)리스트 조회
    @GetMapping("/detail/{id}/members")
    public ResponseEntity<List<LaunchedProjectMemberResponse>> getMembers(@PathVariable Long id){
        List<LaunchedProjectMemberResponse> members = launchedProjectService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }

    // Launched-Project id로 해당 글에 눌린 scrap(사이다) 조회
    @GetMapping("/detail/{id}/scraps")
    public ResponseEntity<List<LaunchedProjectScrapResponse>> getScraps(@PathVariable Long id){
        List<LaunchedProjectScrapResponse> scraps = launchedProjectService.getScraps(id);
        return ResponseEntity.ok(scraps);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ListLaunchedProjectResponse>> getList(@PageableDefault(size=10, sort ="createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<ListLaunchedProjectResponse> launchedProjectList = launchedProjectService.getList(pageable);
        return ResponseEntity.ok(launchedProjectList);
    }

}

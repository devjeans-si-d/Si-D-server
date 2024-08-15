package org.devjeans.sid.domain.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.project.dto.*;
import org.devjeans.sid.domain.project.service.ProjectAcceptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 추후 프로젝트 Controller와 통합 예정
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectAcceptController {
    private final ProjectAcceptService projectAcceptService;
    @PostMapping("/applicant/accept")
    public ResponseEntity<String> acceptApplicant(@RequestBody AcceptApplicantRequest acceptApplicantRequest) {
        projectAcceptService.acceptApplicant(acceptApplicantRequest);
        return new ResponseEntity<>("승인 완료", HttpStatus.OK);
    }

    @GetMapping("/{projectId}/applicants")
    public ResponseEntity<Page<ApplicantResponse>> getApplicants(@PageableDefault(size = 5) Pageable pageable, @PathVariable Long projectId) {
        Page<ApplicantResponse> applicants = projectAcceptService.getApplicants(pageable, projectId);
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    // 내가 참여한 프로젝트 리스트 보기
    @GetMapping("/my-projects")
    public ResponseEntity<Page<MyProjectResponse>> getMyProjects(@PageableDefault(size = 5) Pageable pageable) {
        Page<MyProjectResponse> projectList = projectAcceptService.getMyProjectList(pageable);

        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    // 내 지원 목록 보기
    @GetMapping("/my-applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplicationList(@PageableDefault(size = 5) Pageable pageable) {
        Page<ApplicationResponse> projectList = projectAcceptService.getMyApplicationList(pageable);
        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    // 지원하기
    @PostMapping("/{projectId}/apply")
    public ResponseEntity<ApplyProjectResponse> applyProject(@PathVariable Long projectId, @RequestBody ApplyProjectRequest applyProjectRequest) {
        ApplyProjectResponse applyProjectResponse = projectAcceptService.applyProject(projectId, applyProjectRequest);

        return new ResponseEntity<>(applyProjectResponse, HttpStatus.OK);
    }

}

package org.devjeans.sid.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.project.dto.AcceptApplicantRequest;
import org.devjeans.sid.domain.project.dto.ApplicantResponse;
import org.devjeans.sid.domain.project.dto.MyProjectResponse;
import org.devjeans.sid.domain.project.service.ProjectAcceptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 추후 프로젝트 Controller와 통합 예정
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
    public ResponseEntity<Page<ApplicantResponse>> getApplicants(Pageable pageable, @PathVariable Long projectId) {
        Page<ApplicantResponse> applicants = projectAcceptService.getApplicants(pageable, projectId);
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<MyProjectResponse>> getMyProjects(Pageable pageable) {
        List<MyProjectResponse> projectList = projectAcceptService.getMyProjectList(pageable);
        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }
}

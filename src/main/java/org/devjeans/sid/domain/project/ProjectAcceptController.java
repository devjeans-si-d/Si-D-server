package org.devjeans.sid.domain.project;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.project.dto.AcceptApplicantRequest;
import org.devjeans.sid.domain.project.service.ProjectAcceptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

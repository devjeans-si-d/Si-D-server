package org.devjeans.sid.domain.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.AcceptApplicantRequest;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectApplication;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectApplicationRepository;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.exception.exceptionType.ProjectAcceptException;
import org.devjeans.sid.global.exception.exceptionType.ProjectExceptionType;
import org.devjeans.sid.global.external.mail.service.EmailService;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devjeans.sid.global.exception.exceptionType.ProjectAcceptException.*;

// TODO: 추후 ProjectService로 통합 예정
@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectAcceptService {
    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectApplicationRepository projectApplicationRepository;
    private final EmailService emailService;

    @Transactional
    public void acceptApplicant(AcceptApplicantRequest acceptApplicantRequest) {
        // 검증 1. 현재 로그인한 유저가 해당 프로젝트의 PM이 맞는지?
        Long currentMemberId = securityUtil.getCurrentMemberId();
        Project project = projectRepository.findByIdOrThrow(acceptApplicantRequest.getProjectId());

        log.info("line 38 {}, {}", acceptApplicantRequest.getApplicantId(), acceptApplicantRequest.getProjectId());
        log.info("line 39 {}, {}", project.getId(), project.getProjectMembers());
        for(ProjectMember m : project.getProjectMembers()) {
            log.info("line 43: {}", m.getId());
        }

        if(!project.getPm().getId().equals(currentMemberId)) {
            throw new BaseException(NOT_A_PM_MEMBER);
        }

        // 검증 2. 모집이 끝나지 않았는지?
        if(project.getIsClosed().equals("Y")) {
            throw new BaseException(CLOSED_PROJECT);
        }

        // 검증 3. 해당 유저가 프로젝트에 지원한 내역이 있는지? 이미 승인 받진 않았는지?
        ProjectApplication projectApplication = projectApplicationRepository
                .findProjectApplicationByMemberIdAndProjectId(acceptApplicantRequest.getApplicantId(), acceptApplicantRequest.getProjectId())
                .orElseThrow(() -> new BaseException(NO_APPLICATION_RECORD));
        if(projectApplication.isAccepted()) {
            throw new BaseException(DOUBLE_ACCEPT);
        }

        // 모두 충족이 된다면, ProjectMember 테이블에 저장해주기 && isAccepted true 처리
        projectApplication.updateIsAccepted(true);

        Member member = memberRepository.findByIdOrThrow(acceptApplicantRequest.getApplicantId());
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .member(member)
                .jobField(projectApplication.getJobField())
                .build();

        projectMemberRepository.save(projectMember); // projectMember에 저장

        //== 아래부터는 이메일 로직 ==//
        emailService.sendEmailNoticeForAccept(member.getEmail(), member.getId(), project.getId());
    }

}

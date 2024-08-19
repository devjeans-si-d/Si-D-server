package org.devjeans.sid.domain.mainPage.service;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.launchedProject.service.LaunchedProjectScrapService;
import org.devjeans.sid.domain.launchedProject.service.LaunchedProjectViewService;
import org.devjeans.sid.domain.mainPage.dto.TopListLaunchedProjectResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListMemberResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.project.service.ProjectService;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.siderCard.repository.SiderCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MainPageService {

    private final LaunchedProjectRepository launchedProjectRepository;
    private final MemberRepository memberRepository;
    private final LaunchedProjectViewService launchedProjectViewService;
    private final LaunchedProjectScrapService launchedProjectScrapService;
    private final ProjectService projectService;
    private final SiderCardRepository siderCardRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Autowired
    public MainPageService(LaunchedProjectRepository launchedProjectRepository,
                           MemberRepository memberRepository,
                           LaunchedProjectViewService launchedProjectViewService,
                           LaunchedProjectScrapService launchedProjectScrapService,
                           ProjectService projectService,
                           SiderCardRepository siderCardRepository, ProjectMemberRepository projectMemberRepository){
        this.launchedProjectRepository = launchedProjectRepository;
        this.memberRepository = memberRepository;
        this.launchedProjectViewService = launchedProjectViewService;
        this.launchedProjectScrapService = launchedProjectScrapService;
        this.projectService = projectService;
        this.siderCardRepository = siderCardRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    // 조회수 순으로 8개 Page (+아직 마감/삭제되지 않은 모집공고)
    public Page<ListProjectResponse> getTopProjects(Pageable pageable) {
        // ProjectService의 projectReadAll 메소드를 사용해 프로젝트 리스트를 가져옵니다.
        Page<ListProjectResponse> projectList = projectService.projectReadAll(pageable);

        // 조회수를 기준으로 내림차순 정렬
        List<ListProjectResponse> sortedProjectList = projectList.getContent().stream()
                .sorted(Comparator.comparingLong(ListProjectResponse::getViews).reversed())
                .collect(Collectors.toList());

        // 정렬된 리스트를 다시 Page 객체로 변환하여 반환
        return new PageImpl<>(sortedProjectList, pageable, projectList.getTotalElements());
    }

    public Page<TopListLaunchedProjectResponse> getTopLaunchedProjects(Pageable pageable) {
        // 삭제되지 않은 LaunchedProject 페이지
        Page<LaunchedProject> topLaunchedProjects = launchedProjectRepository.findByDeletedAtIsNull(pageable);

        // TopListLaunchedProjectResponse로 조립
        List<TopListLaunchedProjectResponse> topListLaunchedProjectResponses = topLaunchedProjects.stream()
                .map(l -> l.topListResfromEntity(
                        l,
                        launchedProjectViewService.getViews(l.getId()),
                        launchedProjectScrapService.getScrapCount(l.getId())))
                .sorted(Comparator.comparingLong(TopListLaunchedProjectResponse::getViews).reversed()) // 조회수 기준으로 내림차순 정렬
                .collect(Collectors.toList());

        // List -> Page로 변환
        return new PageImpl<>(topListLaunchedProjectResponses, pageable, topLaunchedProjects.getTotalElements());
    }

    // 진행완료한 프로젝트 개수 많은 회원 순 정렬
    public Page<TopListMemberResponse> getTopMembers(Pageable pageable){
        Page<Member> members = projectMemberRepository.findMembersOrderByProjectCountDesc(pageable);
        Page<TopListMemberResponse> topListMemberResponsePage = members.map(member -> {
            SiderCard siderCard = siderCardRepository.findByIdOrThrow(member.getId());
            return member.topListResFromMember(member, siderCard);
        });
        return topListMemberResponsePage;
    }

}

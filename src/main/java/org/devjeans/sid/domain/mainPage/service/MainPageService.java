package org.devjeans.sid.domain.mainPage.service;

import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.mainPage.dto.TopListLaunchedProjectResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListProjectResponse;
import org.devjeans.sid.domain.mainPage.dto.TopListMemberResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.siderCard.repository.SiderCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MainPageService {

    private final ProjectRepository projectRepository;
    private final LaunchedProjectRepository launchedProjectRepository;
    private final MemberRepository memberRepository;
    private final SiderCardRepository siderCardRepository;

    @Autowired
    public MainPageService(ProjectRepository projectRepository,
                           LaunchedProjectRepository launchedProjectRepository,
                           MemberRepository memberRepository, SiderCardRepository siderCardRepository){
        this.projectRepository = projectRepository;
        this.launchedProjectRepository = launchedProjectRepository;
        this.memberRepository = memberRepository;
        this.siderCardRepository = siderCardRepository;
    }

    // 스크랩 내림차 순으로 4개 Page (+아직 마감/삭제되지 않은 모집공고)
    public Page<TopListProjectResponse> getTopProjects(Pageable pageable){
        Page<Project> topProjects = projectRepository.findTopProjects(pageable);
        Page<TopListProjectResponse> topListProjectResPage = topProjects.map(project->project.topListResFromEntity(project));
        return topListProjectResPage;
    }

    // 스크랩(사이다) 내림차 순으로 4개 Page (+아직 삭제되지 않은 완성 프로젝트 글)
    public Page<TopListLaunchedProjectResponse> getTopLaunchedProjects(Pageable pageable){
        Page<LaunchedProject> topLaunchedProjects = launchedProjectRepository.findTopLaunchedProjects(pageable);
        Page<TopListLaunchedProjectResponse> topListLaunchedProjectResPage = topLaunchedProjects.map(launchedProject->launchedProject.topListResfromEntity(launchedProject));
        return topListLaunchedProjectResPage;
    }

    // 최신회원 3명
    public Page<TopListMemberResponse> getTopMembers(Pageable pageable){
        Page<Member> members = memberRepository.findAllByOrderByUpdatedAtDesc(pageable);
        Page<TopListMemberResponse> topListMemberResponsePage = members.map(member -> member.topListResFromMember(member));
        return topListMemberResponsePage;
    }

}

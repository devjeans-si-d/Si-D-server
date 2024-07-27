package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.siderCard.entity.JobField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
//Todo ResponseEntity() 적용
@Service
@Transactional
public class ProjectService {
    // 나머지 repository는 필요할 때 가져오기!!!!!
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    @Autowired
    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
    }
    //create
    public Project projectCreate(CreateProjectRequest dto){
        Member member = memberRepository.findById(dto.getPmId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
        // Todo : s3 쓰면 string, 현재는 테스트를 위해 파일 입력 코드 필요
        Project project = dto.toEntity(member);

        ProjectMember pmToAddProjectMember = ProjectMember.builder().member(member).jobField(JobField.PM).project(project).build();
        project.getProjectMembers().add(pmToAddProjectMember);

        for(CreateProjectRequest.ProjectMemberCreateRequest inputMember: dto.getProjectMembers()){
            Member memberInput = memberRepository.findById(inputMember.getMemberId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
            ProjectMember projectMember = ProjectMember.builder().member(memberInput).project(project).jobField(inputMember.getJobField()).build();
            project.getProjectMembers().add(projectMember);
        }

        for(CreateProjectRequest.RecruitInfoCreateRequest recruitInfoInput : dto.getRecruitInfos()){
            // jobfield, count, project
            RecruitInfo recruitInfo = RecruitInfo.builder()
                    .jobField(recruitInfoInput.getJobField())
                    .project(project)
                    .count(recruitInfoInput.getCount())
                    .build();
            project.getRecruitInfos().add(recruitInfo);
        }

        Project savedProject = projectRepository.save(project);
        return savedProject;

    }

    // read-detail
    public DetailProjectResponse projectReadDetail(Long id){
        // Todo 에러처리
        // Todo 추후 isclosed 체크
        Project project = projectRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
        return DetailProjectResponse.fromEntity(project);
    }

    // read-list
    // Todo page 적용
    public List<ListProjectResponse> projectReadAll(){
        List<ListProjectResponse> listProjectResponses = new ArrayList<>();
        // Todo isclosed 체크 & 최신순 정렬
        List<Project> projectList = projectRepository.findAll();
        for(Project project : projectList){
            ListProjectResponse listProjectResponse = ListProjectResponse.builder()
                    .projectName(project.getProjectName())
                    .projectImage(project.getProjectImage())
                    .views(project.getViews())
                    .scrapCount(project.getProjectScraps().size())
                    .isClosed(project.getIsClosed())
                    .description(project.getDescription())
                    .deadline(project.getDeadline())
                    .build();
            listProjectResponses.add(listProjectResponse);
        }
        return listProjectResponses;
    }


}

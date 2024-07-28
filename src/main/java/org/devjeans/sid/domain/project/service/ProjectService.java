package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.dto.read.DetailProjectResponse;
import org.devjeans.sid.domain.project.dto.read.ListProjectResponse;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectRequest;
import org.devjeans.sid.domain.project.dto.update.UpdateProjectResponse;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.RecruitInfo;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.devjeans.sid.domain.project.repository.RecruitInfoRepository;
import org.devjeans.sid.domain.siderCard.entity.JobField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.devjeans.sid.domain.projectMember.entity.ProjectMember;

import java.time.LocalDateTime;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitInfoRepository recruitInfoRepository;
    @Autowired
    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository, ProjectMemberRepository projectMemberRepository, RecruitInfoRepository recruitInfoRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.recruitInfoRepository = recruitInfoRepository;
    }
    //create
    // Todo : imageurl multipart
    public Project projectCreate(CreateProjectRequest dto){
        Member pm = memberRepository.findById(dto.getPmId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
        // Todo : s3 쓰면 string, 현재는 테스트를 위해 파일 입력 코드 필요
        Project project = dto.toEntity(pm);

        //create시 projectMember에 pm 자동 add
        ProjectMember pmToAddProjectMember = ProjectMember.builder().member(pm).jobField(JobField.PM).project(project).build();
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
    // Todo :  deletedAt==null 인 것만 조회
    public DetailProjectResponse projectReadDetail(Long id){
        // Todo 에러처리
        // Todo 추후 isclosed 체크
        Project project = projectRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
        return DetailProjectResponse.fromEntity(project);
    }

    // read-list
    // Todo page 적용 & deletedAt==null 인 것만 조회
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

    // update project
    // Todo : imageurl multipart
    public UpdateProjectResponse projectUpdate(UpdateProjectRequest dto,Long projectId){
        // Todo : dto랑 실제 고치려는 프로젝트랑 체크해서 다르면 error 날려야하는데 아직 unique값이 없어서 나중에 처리
        Project project=projectRepository.findById(projectId).orElseThrow(()->new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다")); //Todo : 에러 처리 컨벤션 적용

        // projectMember 조립
        for(UpdateProjectRequest.ProjectMemberDeleteRequest projectMember: dto.getDeleteProjectMembers()){
            // 성공
            ProjectMember toDeleteProjectMember = projectMemberRepository.findById(projectMember.getId()).orElseThrow(()->new EntityNotFoundException("해당 프로젝트 멤버가 없습니다."));
            project.getProjectMembers().remove(toDeleteProjectMember);
            projectMemberRepository.delete(toDeleteProjectMember);
        }
        for(UpdateProjectRequest.ProjectMemberAddRequest projectMember: dto.getAddProjectMembers()){
            Member memberForAddProjectMember = memberRepository.findById(projectMember.getMemberId()).orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다"));
            ProjectMember toAddProjectMember = ProjectMember.builder().member(memberForAddProjectMember).project(project).jobField(projectMember.getJobField()).build();
            project.getProjectMembers().add(projectMemberRepository.save(toAddProjectMember));
        }


        // recruitinfo 조립
        for(UpdateProjectRequest.RecruitInfoAddRequest recruitInfo : dto.getAddRecruitInfos()){
//            Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다"));
            RecruitInfo toAddRecruitInfo = RecruitInfo.builder().jobField(recruitInfo.getJobField()).count(recruitInfo.getCount()).project(project).build();
            project.getRecruitInfos().add(recruitInfoRepository.save(toAddRecruitInfo));
        }
        for(UpdateProjectRequest.RecruitInfoDeleteRequest recruitInfo : dto.getDeleteRecruitInfos()){
            // 성공
            RecruitInfo toDeleteRecruitInfo = recruitInfoRepository.findById(recruitInfo.getId()).orElseThrow(()->new EntityNotFoundException("해당 프로젝트에 해당 리쿠르트 정보가 없습니다."));
            project.getRecruitInfos().remove(toDeleteRecruitInfo);
            recruitInfoRepository.delete(toDeleteRecruitInfo);
        }
        Project updatedProject=UpdateProjectRequest.updateProject(project,dto);

        Project toCheckUpdateProject = projectRepository.save(updatedProject);
        return UpdateProjectResponse.fromEntity(toCheckUpdateProject);
    }

    // add projectmember
    public void addProjectMember(Long memberId, Long projectId,JobField jobField){
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다"));
        ProjectMember projectMember = ProjectMember.builder().member(member).project(project).jobField(jobField).build();
        project.getProjectMembers().add(projectMember);
    }

    // delete projectmember  - projectmemberId
    public void deleteProjectMember(Long projectMemberId,Long projectId){
        ProjectMember toDelete = projectMemberRepository.findById(projectMemberId).orElseThrow(()->new EntityNotFoundException("해당 프로젝트 멤버가 없습니다."));
        Project changeProject = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다"));
        changeProject.getProjectMembers().remove(toDelete);
        projectMemberRepository.delete(toDelete);
    }

    //add recruitinfo
    public void addRecruitInfo(JobField jobField,int count,Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다"));
        RecruitInfo recruitInfo = RecruitInfo.builder().jobField(jobField).count(count).project(project).build();
        project.getRecruitInfos().add(recruitInfo);
    }

    // delete recruitinfo
    public void deleteRecruitInfo(Long recruitInfoId, Long projectId){
        RecruitInfo toDelete = recruitInfoRepository.findById(recruitInfoId).orElseThrow(()->new EntityNotFoundException("해당 프로젝트에 해당 리쿠르트 정보가 없습니다."));
        Project changeProject = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트가 없습니다"));
        changeProject.getRecruitInfos().remove(toDelete);
        recruitInfoRepository.delete(toDelete);
    }

    // delete project by deletedAt
    // Todo projectid 연동된 projectmember, recruitmember 같이 삭제
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 프로젝트가 없습니다."));
        if (project.getDeletedAt()==null) {
            project.setDeletedAt(LocalDateTime.now());
            projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("해당 프로젝트는 이미 삭제되었습니다.");
        }
    }


}

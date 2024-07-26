package org.devjeans.sid.domain.project.service;

import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.dto.create.CreateProjectRequest;
import org.devjeans.sid.domain.project.entity.Project;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
    public Project projectCreate(CreateProjectRequest dto){
        Member member = memberRepository.findById(dto.getPmId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
        Project project = dto.toEntity(member);

        for(Member projectMember: dto.getProjectMembers()){
            ProjectMember member1 = ProjectMember.builder().member(projectMember).project(project).build();
            project.getProjectMembers().add(member1);
        }
        Project savedProject = projectRepository.save(project);
        return savedProject;

    }
}

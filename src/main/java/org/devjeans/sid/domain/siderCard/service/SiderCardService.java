package org.devjeans.sid.domain.siderCard.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.launchedProject.entity.LaunchedProject;
import org.devjeans.sid.domain.launchedProject.repository.LaunchedProjectRepository;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.project.entity.ProjectMember;
import org.devjeans.sid.domain.project.repository.ProjectMemberRepository;
import org.devjeans.sid.domain.siderCard.dto.*;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.siderCard.entity.SiderCardTechStack;
import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.domain.siderCard.repository.CareerRepository;
import org.devjeans.sid.domain.siderCard.repository.SiderCardRepository;
import org.devjeans.sid.domain.siderCard.repository.SiderCardTechStackRepository;
import org.devjeans.sid.domain.siderCard.repository.TechStackRepository;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SiderCardService {

    private final SiderCardRepository siderCardRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final CareerRepository careerRepository;
    private final SiderCardTechStackRepository siderCardTechStackRepository;
    private final TechStackRepository techStackRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final LaunchedProjectRepository launchedProjectRepository;

    public SiderCardResDto getSiderCard(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member not found"));
        SiderCard siderCard = siderCardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("SiderCard not found"));
        List<ProjectMember> projectMemberList = projectMemberRepository.findAllByMemberId(id);
        List<LaunchedProjectResDto> launchedProjectResDtoList = new ArrayList<>();
        for(ProjectMember projectMember : projectMemberList) {
            LaunchedProject launchedProject = launchedProjectRepository.findByProjectIdAndDeletedAtIsNull(projectMember.getProject().getId()).orElse(null);
            if(launchedProject != null) {
                launchedProjectResDtoList.add(new LaunchedProjectResDto(launchedProject.getId(),launchedProject.getLaunchedProjectImage()));
            }
        }
        return siderCard.fromEntity(member,launchedProjectResDtoList);
    }

    public SiderCardResDto updateSiderCard(SiderCardUpdateReqDto dto) {
        Long id = securityUtil.getCurrentMemberId();
        SiderCard siderCard = siderCardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("SiderCard not found"));
        careerRepository.deleteCareerBySiderCard(siderCard);
        siderCardTechStackRepository.deleteSiderCardTechStackBySiderCard(siderCard);
        List<SiderCardTechStack> techStacks = new ArrayList<>();
        for(TeckStackReqDto teckStack: dto.getTeckStacks()){
            TechStack ts = techStackRepository.findByIdOrThrow(teckStack.getId());
            techStacks.add(SiderCardTechStack.builder().techStack(ts).siderCard(siderCard).build());
        }
        siderCard.update(dto,siderCard,techStacks);
        return getSiderCard(id);
    }

    public Page<SiDCardListDto> getSiderCardList(Pageable pageable) {
        Page<SiderCard> siderCards = siderCardRepository.findAll(pageable);
        Page<SiDCardListDto> siderCardListDtos = siderCards.map(siderCard->{
            Member member = memberRepository.findById(siderCard.getId()).orElseThrow(()->new EntityNotFoundException("member not found"));
            return siderCard.listFromEntity(member);
        });
//        for(SiderCard siderCard: siderCards){
//            Member member = memberRepository.findById(siderCard.getId()).orElseThrow(()->new EntityNotFoundException("member not found"));
//            siderCardListDtos.add(siderCard.listFromEntity(member));
//        }
        return siderCardListDtos;
    }
}

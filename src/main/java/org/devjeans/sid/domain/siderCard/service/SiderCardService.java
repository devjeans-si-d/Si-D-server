package org.devjeans.sid.domain.siderCard.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.dto.SiderCardUpdateReqDto;
import org.devjeans.sid.domain.siderCard.dto.TeckStackReqDto;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.siderCard.entity.SiderCardTechStack;
import org.devjeans.sid.domain.siderCard.entity.TechStack;
import org.devjeans.sid.domain.siderCard.repository.CareerRepository;
import org.devjeans.sid.domain.siderCard.repository.SiderCardRepository;
import org.devjeans.sid.domain.siderCard.repository.SiderCardTechStackRepository;
import org.devjeans.sid.domain.siderCard.repository.TechStackRepository;
import org.devjeans.sid.global.util.SecurityUtil;
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

    public SiderCardResDto getSiderCard() {
        Long id = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member not found"));
        SiderCard siderCard = siderCardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("SiderCard not found"));
        return siderCard.fromEntity(member);
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
        return getSiderCard();
    }
}

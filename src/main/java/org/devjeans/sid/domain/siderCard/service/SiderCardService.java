package org.devjeans.sid.domain.siderCard.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.domain.siderCard.dto.SiderCardResDto;
import org.devjeans.sid.domain.siderCard.entity.SiderCard;
import org.devjeans.sid.domain.siderCard.repository.SiderCardRepository;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class SiderCardService {

    private final SiderCardRepository siderCardRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;

    public SiderCardResDto getSiderCard() {
        Long id = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member not found"));
        SiderCard siderCard = siderCardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("SiderCard not found"));
        return siderCard.fromEntity(member);
    }
}

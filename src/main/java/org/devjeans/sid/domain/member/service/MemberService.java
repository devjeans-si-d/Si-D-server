package org.devjeans.sid.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberInfoResponse getMemberInfo(Long memberId) {
        // TODO: 인증, 인가 구현 후 멤버 아이디와 시큐리티 컨텍스트의 멤버가 동일한지 확인하는 로직 필요

        Member member = memberRepository.findByIdOrThrow(memberId);
        return MemberInfoResponse.fromEntity(member);
    }
}

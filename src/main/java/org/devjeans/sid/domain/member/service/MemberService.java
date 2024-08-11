package org.devjeans.sid.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.member.dto.*;
import org.devjeans.sid.domain.auth.entity.KakaoProfile;
import org.devjeans.sid.domain.auth.entity.KakaoRedirect;
import org.devjeans.sid.domain.member.entity.Member;
import org.devjeans.sid.domain.auth.entity.OAuthToken;
import org.devjeans.sid.domain.member.repository.MemberRepository;
import org.devjeans.sid.global.exception.BaseException;
import org.devjeans.sid.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devjeans.sid.global.exception.exceptionType.MemberExceptionType.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, MemberIdEmailCode> redisTemplate;
    private final SecurityUtil securityUtil;

    public MemberInfoResponse getMemberInfo() {
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findByIdOrThrow(memberId);
        return MemberInfoResponse.fromEntity(member);
    }

    @Transactional
    public UpdateMemberResponse updateMemberInfo(UpdateMemberRequest updateMemberRequest) {
        Long memberId = securityUtil.getCurrentMemberId();

        Member member = memberRepository.findByIdOrThrow(memberId);

        // 회원 정보 수정
        member.updateMemberInfo(updateMemberRequest);
        Member updatedMember = memberRepository.save(member);

        return UpdateMemberResponse.fromEntity(updatedMember);
    }
    @Transactional
    public UpdateEmailResponse updateMemberEmail(String code) {
//        Long memberId = securityUtil.getCurrentMemberId();
        Long memberId = 1L;
        Member member = memberRepository.findByIdOrThrow(memberId);

        if(getMemberIdFromRedis(code).equals(memberId)) { // 요청 멤버와 현재 멤버가 같다면
            String updatedEmail = getEmailFromRedis(code);
            member.updateEmail(updatedEmail); // 인증 성공
            return new UpdateEmailResponse(memberId, updatedEmail);
        } else {
            throw new BaseException(INVALID_VERIFICATION);
        }
    }

    private String getEmailFromRedis(String code) {
        MemberIdEmailCode ret = (MemberIdEmailCode) redisTemplate.opsForValue().get(code);
        assert ret != null;
        return ret.getEmail();
    }

    private Long getMemberIdFromRedis(String code) {
        MemberIdEmailCode ret = (MemberIdEmailCode) redisTemplate.opsForValue().get(code);
        assert ret != null;
        return ret.getMemberId();
    }
}

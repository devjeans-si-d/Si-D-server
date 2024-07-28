package org.devjeans.sid.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.devjeans.sid.domain.member.dto.MemberInfoResponse;
import org.devjeans.sid.domain.member.dto.RegisterMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberRequest;
import org.devjeans.sid.domain.member.dto.UpdateMemberResponse;
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


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityUtil securityUtil;

    @Value("${auth.oauth.kakao.api}")
    private String authOauthKakaoApi;
    public MemberInfoResponse getMemberInfo(Long memberId) {
        // TODO: 인증, 인가 구현 후 멤버 아이디와 시큐리티 컨텍스트의 멤버가 동일한지 확인하는 로직 필요

        Member member = memberRepository.findByIdOrThrow(memberId);
        return MemberInfoResponse.fromEntity(member);
    }

    @Transactional
    public UpdateMemberResponse updateMemberInfo(Long memberId,
                                                 UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        // 회원 정보 수정
        member.updateMemberInfo(updateMemberRequest);
        Member updatedMember = memberRepository.save(member);

        return UpdateMemberResponse.fromEntity(updatedMember);
    }
    @Transactional
    public void updateEmail(String email, String code) {
        Long memberId = securityUtil.getCurrentMemberId();
        Member member = memberRepository.findByIdOrThrow(memberId);
        if(getEmailCodeFromRedis(memberId).equals(code)) {
            member.updateEmail(email); // 인증 성공
        } else {
            throw new BaseException(INVALID_VERIFICATION);
        }
    }

    private String getEmailCodeFromRedis(Long memberId) {
        return (String) redisTemplate.opsForValue().get(memberId.toString());
    }
}
